package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Repository.CategoryRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dany on 15/05/2018.
 */
@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TopicService topicService;
    public List<Category> fetchAllCategories(){
        return categoryRepository.findAll();
    }

    public Category fetchCategory(Long id){
        return categoryRepository.findOne(id);
    }

    public Pair<String, Category> updateCategory (Category category){
        Category savedCategory = categoryRepository.findCategoryByNameAndIdIsNot(category.getName(), category.getId());

        if (savedCategory != null) {
            return new Pair<>("The category name already exist for another definition", null);
        }

        Category currentCategory= categoryRepository.findOne(category.getId());

        if (currentCategory == null) {
            return new Pair<>("The category you want to update doesn't exist", null);
        }
        currentCategory.setDescription(category.getDescription());
        currentCategory.setName(category.getName());
        currentCategory.setParentCategory(category.getParentCategory());

        Category result = categoryRepository.save(category);
        if (result == null) {
            return new Pair<>("Couldn't update the category", null);
        } else {
            return new Pair<>("Category updated successfully", result);
        }
    }

    public Pair<String, Category>  saveCategory (Category category){
        if (category.getId() > 0L) {
            return updateCategory(category);
        } else {
            Category savedCategory = categoryRepository.findCategoryByName(category.getName());

            if ( savedCategory != null){
                return new Pair<>("The category you are trying to save already exist", null);
            }
            Category result = categoryRepository.save(category);
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
        List<Category> categories = categoryRepository.findAllByParentCategory(category.getParentCategory().getId());
        categories.forEach(cat -> {
            cat.setParentCategory(null);
            categoryRepository.save(cat);
        });
        category.setDeleted(true);
        categoryRepository.save(category);
        return "Section successfully deleted";
    }
    //other delete methods
    //other fetching methods

}
