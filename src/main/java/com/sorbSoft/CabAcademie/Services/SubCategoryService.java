package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.SubCategory;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Repository.SubCategoryRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.SubCategoryFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubCategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.SubCategoryMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubCategoryViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

    @Autowired
    public List<SubCategory> fetchAllSubCategories(){
        return subCategoryRepository.findAll();
    }

    public SubCategory fetchSubCategory(Long id){
        return subCategoryRepository.findOne(id);
    }

    public Pair<String, SubCategory> updateSubCategory (SubCategoryViewModel vm){
        SubCategory savedSubCategory = subCategoryRepository.findSubCategoryByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedSubCategory != null) {
            return Pair.of("The subCategory name already exist for another definition", null);
        }

        SubCategory currentSubCategory= subCategoryRepository.findOne(vm.getId());

        if (currentSubCategory == null) {
            return Pair.of("The subCategory you want to update doesn't exist", null);
        }
        SubCategory result = subCategoryRepository.save(getEntity(vm));
        if (result == null) {
            return Pair.of("Couldn't update the subCategory", null);
        } else {
            return Pair.of("SubCategory updated successfully", result);
        }
    }

    public Pair<String, SubCategory>  saveSubCategory (SubCategoryViewModel vm){
        if (vm.getId() > 0L) {
            return updateSubCategory(vm);
        } else {
            SubCategory savedSubCategory = subCategoryRepository.findSubCategoryByName(vm.getName());

            if ( savedSubCategory != null){
                return Pair.of("The subCategory you are trying to save already exist", null);
            }

            SubCategory result = subCategoryRepository.save(getEntity(vm));
            if (result == null){
                return Pair.of("Couldn't save the subCategory", null);
            } else {
                return  Pair.of("SubCategory saved successfully", result);
            }
        }
    }

    public String deleteSubCategory(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the subCategory";
        }
        SubCategory subCategory = subCategoryRepository.findOne(id);
        if (subCategory == null) {
            return "The subCategory you want to delete doesn't exist";
        }
//        if (subCategory.getCategory()!= null){
//            List<SubCategory> subCategories = subCategoryRepository.findAllByCategory(subCategory.getParentSubCategory().getId());
//            subCategories.forEach(cat -> {
//                cat.setParentSubCategory(null);
//                subCategoryRepository.save(cat);
//            });
//        }
        subCategory.setDeleted(true);
        subCategoryRepository.save(subCategory);
        return "Section successfully deleted";
    }

    public SubCategoryViewModel getSubCategoryViewModel(Long subCategoryId){
        SubCategoryViewModel vm = SubCategoryFactory.getSubCategoryViewModel();
        if (subCategoryId != null && subCategoryId > 0) {
            SubCategory subCategory = this.fetchSubCategory(subCategoryId);
            if (subCategory == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(subCategory);
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
        SubCategory resultSubCategory = mapper.mapToEntity(vm);
        if (vm.getCategory()!= null && vm.getCategory().getId() != null && vm.getCategory().getId() > 0){
            resultSubCategory.setCategory(categoryRepository.findOne(vm.getCategory().getId()));
        }
        return resultSubCategory;
    }
}
