package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.SubCategory;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Repository.SubCategoryRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SubCategoryFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubCategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SubCategoryMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubCategoryViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dany on 15/05/2018.
 */
@Service
@Transactional
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private SubCategoryMapper mapper
            = Mappers.getMapper(SubCategoryMapper.class);

    public List<SubCategory> fetchAllSubCategories(){
        return subCategoryRepository.findAll();
    }

    public SubCategory fetchSubCategory(Long id){
        return subCategoryRepository.getOne(id);
    }


    public Result updateSubCategory (SubCategoryViewModel vm){
        Result result = new Result();
        SubCategory current = subCategoryRepository.getOne(vm.getId());
        if (current == null) {
            result.add("The sub-category you want to update does not exist");
            return result;
        }
        SubCategory savedSubCategory = subCategoryRepository.findSubCategoryByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedSubCategory != null) {
            result.add("The subCategory name already exist for another definition");
            return result;
        }

        return save(vm);
    }

    public Result saveSubCategory (SubCategoryViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateSubCategory(vm);
        } else {
            SubCategory savedSubCategory = subCategoryRepository.findSubCategoryByName(vm.getName());

            if ( savedSubCategory != null){
                result.add("The subCategory you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    private  Result save (SubCategoryViewModel vm) {
        Result result = new Result();
        try {
           subCategoryRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            return result.add(ex.getMessage());
        }
        return result;
    }

    public Result deleteSubCategory(Long id){
        Result result = new Result();
        if (id <= 0L) {
            result.add("You should indicate the id of the subCategory");
            return result;
        }
        SubCategory subCategory = subCategoryRepository.getOne(id);
        if (subCategory == null) {
            result.add("The subCategory you want to delete doesn't exist");
            return result;
        }
        subCategory.setDeleted(true);
        try {
            subCategoryRepository.save(subCategory);
        } catch (Exception ex)  {
            return result.add(ex.getMessage());
        }
        return result;
    }

    public SubCategoryViewModel getSubCategoryViewModel(Long subCategoryId){
        SubCategoryViewModel vm = SubCategoryFactory.getSubCategoryViewModel();
        if (subCategoryId != null && subCategoryId > 0) {
            SubCategory subCategory = this.fetchSubCategory(subCategoryId);
            if (subCategory == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(subCategory);
                if (vm.getCategory()== null) {
                    Category category = new Category();
                    category.setId(0L);
                    vm.setCategory(category);
                }
            }
        }
        vm.setAllSubCategories(getAllFiltered(subCategoryId));
        vm.setAllCategories(categoryRepository.findAll());
        return vm;
    }

    public List<SubCategory> getAllFiltered(Long subCategoryId){
        List<SubCategory> subCategories = fetchAllSubCategories();
        if (subCategoryId == null)
            return subCategories;
        List<SubCategory> subCategoriesFiltered = subCategories.isEmpty() ? null :  subCategories.stream().filter(o -> !o.getId().equals(subCategoryId)).collect(Collectors.toList());
        if (subCategoriesFiltered == null) {
            return new ArrayList<>();
        }
        return subCategoriesFiltered;  //.stream().filter(o -> o.getParentSubCategory() == null || !o.getParentSubCategory().getId().equals(subCategoryId)).collect(Collectors.toList());
    }

    public List <SubCategoryInfo> getSubCategoryInfo(){
        List<SubCategory> subCategories = this.fetchAllSubCategories();
        if (subCategories.isEmpty()) {
            return new ArrayList<>();
        }
        List<SubCategoryInfo> info = new ArrayList<>();
        for (SubCategory sub : subCategories) {
            SubCategoryInfo sInfo = mapper.mapEntityToInfo(sub);
            info.add(sInfo);
            // TODO: Set the subCategory info list
        }
        return info;
    }


    private SubCategory getEntity(SubCategoryViewModel vm){
        return mapper.mapToEntity(vm);
//        if (vm.getCategory()!= null && vm.getCategory().getId() != null && vm.getCategory().getId() > 0){
//            resultSubCategory.setCategory(categoryRepository.getOne(vm.getCategory().getId()));
//        }
//        return resultSubCategory;
    }

    private Result ValidateModel(SubCategoryViewModel vm){
        Result result = new Result();

        if (vm.getName().isEmpty()) {
            result.add("You should specify the sub-category name");
            return result;
        }

        if (vm.getCategory().getId() <= 0L) {
            result.add("You should specify the category");
            return result;
        }

        Category category = categoryRepository.getOne(vm.getCategory().getId());
        if (category == null) {
            result.add("The category you specified does not exist");
            return result;
        }

        if (vm.getSubCategories().size() > 0) {
            for (SubCategory subCategory : vm.getSubCategories()) {
                SubCategory sub = subCategoryRepository.getOne(subCategory.getId());
                if (sub == null) {
                    result.add(String.format("The sub-category no. {0} in the list of sub-categories does not exist", vm.getSubCategories().indexOf(subCategory) + 1 ));
                    return result;
                }
                if (vm.getId() > 0L && vm.getId().equals(subCategory.getId())) {
                    result.add(String.format("The sub-category {0} cannot be a sub-category of himself", vm.getName()));
                    return result;
                }
            }
        }

        return result;
    }


    public SubCategoryViewModel prepareEntity(SubCategoryViewModel vm) {
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getDescription() == null)  {
            vm.setDescription("");
        }
        if (vm.getCategory() == null) {
            vm.setCategory( new Category(){
                @Override
                public Long getId() {
                    return 0L;
                }
            });
        }
        if (vm.getSubCategories()== null) {
            vm.setSubCategories(new ArrayList<>());
        }
        return vm;
    }

}
