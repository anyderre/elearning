package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CategoryFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.CategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CategoryMapper;
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

    public Pair<String, Category> updateCategory (CategoryViewModel vm){
        Category savedCategory = categoryRepository.findCategoryByNameAndIdIsNot(vm.getName(), vm.getId());

        if (savedCategory != null) {
            return Pair.of("The category name already exist for another definition", null);
        }

        Category currentCategory= categoryRepository.findOne(vm.getId());

        if (currentCategory == null) {
            return Pair.of("The category you want to update doesn't exist", null);
        }
        Category result = categoryRepository.save(getEntity(vm));
        if (result == null) {
            return Pair.of("Couldn't update the category", null);
        } else {
            return Pair.of("Category updated successfully", result);
        }
    }

    public Pair<String, Category>  saveCategory (CategoryViewModel vm){
        if (vm.getId() > 0L) {
            return updateCategory(vm);
        } else {
            Category savedCategory = categoryRepository.findCategoryByName(vm.getName());

            if ( savedCategory != null){
                return Pair.of("The category you are trying to save already exist", null);
            }

            Category result = categoryRepository.save(getEntity(vm));
            if (result == null){
                return Pair.of("Couldn't save the category", null);
            } else {
                return  Pair.of("Category saved successfully", result);
            }
        }
    }

    public String deleteCategory(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the category";
        }
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            return "The category you want to delete doesn't exist";
        }
        category.setDeleted(true);
        categoryRepository.save(category);
        return "Category successfully deleted";
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
}
