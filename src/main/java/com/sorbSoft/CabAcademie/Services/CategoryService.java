package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CategoryFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.CategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CategoryMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryMapper mapper
            = Mappers.getMapper(CategoryMapper.class);

    @Autowired
    public List<Category> fetchAllCategories(){
        return categoryRepository.findAll();
    }

    public Category fetchCategory(Long id){
        return categoryRepository.findOne(id);
    }

    public Result updateCategory (CategoryViewModel vm){
        Result result = new Result();
        Category current = categoryRepository.findOne(vm.getId());
        if (current == null) {
            result.add("The category you want to update does not exist");
            return result;
        }

        Category savedCategory = categoryRepository.findCategoryByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedCategory != null) {
            result.add("The category name already exist for another definition");
            return result;
        }

        Category currentCategory= categoryRepository.findOne(vm.getId());

        if (currentCategory == null) {
            result.add("The category you want to update doesn't exist");
            return result;
        }
        return save(vm);
    }

    public Result  saveCategory (CategoryViewModel vm){
        vm = prepareEntity(vm);
        Result result = ValidateModel(vm);
        if (!result.isValid()) {
            return result;
        }
        if (vm.getId() > 0L) {
            return updateCategory(vm);
        } else {
            Category savedCategory = categoryRepository.findCategoryByName(vm.getName());

            if ( savedCategory != null){
                result.add("The category you are trying to save already exist");
                return result;
            }

            return save(vm);
        }
    }

    private  Result save (CategoryViewModel vm) {
        Result result = new Result();
        try {
            categoryRepository.save(getEntity(vm));
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public Result deleteCategory(Long id){
        Result result = new Result();
        if (id <= 0L) {
            result.add("You should indicate the id of the category");
            return result;
        }
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            result.add("The category you want to delete doesn't exist");
            return result;
        }
        category.setDeleted(true);
        try {
            categoryRepository.save(category);
        } catch (Exception ex)  {
            result.add(ex.getMessage());
            return result;
        }
        return result;
    }

    public CategoryViewModel getCategoryViewModel(Long categoryId){
        CategoryViewModel vm = CategoryFactory.getCategoryViewModel();
        if (categoryId != null && categoryId > 0) {
            Category category = this.fetchCategory(categoryId);
            if (category == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(category);
            }
        }
        return vm;
    }

    public List<Category> getAllFiltered(Long categoryId){
        List<Category> categories = fetchAllCategories();
        if (categoryId == null)
            return categories;
        List<Category> categoriesFiltered = categories.isEmpty() ? null :  categories.stream().filter(o -> !o.getId().equals(categoryId)).collect(Collectors.toList());
        if (categoriesFiltered == null) {
            return new ArrayList<>();
        }
        return categoriesFiltered;
    }

    public List <CategoryInfo> getUserInfo(){
        List<Category> categories = this.fetchAllCategories();
        if (categories.isEmpty()) {
            return new ArrayList<>();
        }
        List<CategoryInfo> info = new ArrayList<>();
        for (Category category : categories) {
            CategoryInfo cInfo = new CategoryInfo();
            cInfo.setId(category.getId());
            cInfo.setName(category.getName());
            cInfo.setDescription(category.getDescription());
            info.add(cInfo);
        }
        return info;
    }

    public List <CategoryInfo> getCategoryInfo(){
        List<Category> sections = this.fetchAllCategories();
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<CategoryInfo> info = new ArrayList<>();
        for (Category section : sections) {
            CategoryInfo sInfo = mapper.mapEntityToInfo(section);
            info.add(sInfo);
        }
        return info;
    }

    private Category getEntity(CategoryViewModel vm){
        return mapper.mapToEntity(vm);
    }


    private Result ValidateModel(CategoryViewModel vm){
        Result result = new Result();

        if (vm.getName().isEmpty()) {
            result.add("You should specify the category name");
            return result;
        }

        return result;
    }

    public CategoryViewModel prepareEntity(CategoryViewModel vm) {
        if (vm.getName()== null)  {
            vm.setName("");
        }
        if (vm.getId()== null)  {
            vm.setId(0L);
        }
        if (vm.getDescription() == null)  {
            vm.setDescription("");
        }
        return vm;
    }
}
