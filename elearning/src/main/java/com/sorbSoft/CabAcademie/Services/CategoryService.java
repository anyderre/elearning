package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.CategoryFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.CategoryMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import javafx.util.Pair;
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
            return new Pair<>("The category name already exist for another definition", null);
        }

        Category currentCategory= categoryRepository.findOne(vm.getId());

        if (currentCategory == null) {
            return new Pair<>("The category you want to update doesn't exist", null);
        }

        Category resultCategory = mapper.mapToEntity(vm);
//        currentCategory.setDescription(category.getDescription());
//        currentCategory.setName(category.getName());
//        currentCategory.setParentCategory(category.getParentCategory());
        Category current = categoryRepository.findOne(vm.getId());
        current.setParentCategory(resultCategory.getParentCategory());
        current.setDescription(resultCategory.getDescription());
        current.setName(resultCategory.getName());
        Category result = categoryRepository.save(resultCategory);
        if (result == null) {
            return new Pair<>("Couldn't update the category", null);
        } else {
            return new Pair<>("Category updated successfully", result);
        }
    }

    public Pair<String, Category>  saveCategory (CategoryViewModel vm){
        if (vm.getId() > 0L) {
            return updateCategory(vm);
        } else {
            Category savedCategory = categoryRepository.findCategoryByName(vm.getName());

            if ( savedCategory != null){
                return new Pair<>("The category you are trying to save already exist", null);
            }
            Category resultCategory = mapper.mapToEntity(vm);
            if (vm.getParentCategory()!= null && vm.getParentCategory().getId() != null && vm.getParentCategory().getId() > 0){
                resultCategory.setParentCategory(categoryRepository.findOne(vm.getParentCategory().getId()));
            }
            Category result = categoryRepository.save(resultCategory);
            if (result == null){
                return new Pair<>("Couldn't save the category", null);
            } else {
                return  new Pair<>("Category saved successfully", result);
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
        if (category.getParentCategory()!= null){
            List<Category> categories = categoryRepository.findAllByParentCategory(category.getParentCategory().getId());
            categories.forEach(cat -> {
                cat.setParentCategory(null);
                categoryRepository.save(cat);
            });
        }
        category.setDeleted(true);
        categoryRepository.save(category);
        return "Section successfully deleted";
    }

    public CategoryViewModel getCategoryViewModel(Long categoryId){
        CategoryViewModel vm = CategoryFactory.getCategoryViewModel();
        if (categoryId != null && categoryId > 0) {
            Category category = this.fetchCategory(categoryId);
            if (category == null) {
                return null;
            } else {
//                vm.setCategories(getAllFiltered(categoryId));
                vm = mapper.mapToViewModel(category);
//                return vm;
            }
        }
        vm.setCategories(getAllFiltered(categoryId));
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
        return categoriesFiltered.stream().filter(o -> o.getParentCategory() == null || !o.getParentCategory().getId().equals(categoryId)).collect(Collectors.toList());
    }

}
