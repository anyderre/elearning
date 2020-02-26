package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Topic;
import com.sorbSoft.CabAcademie.Services.CategoryService;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import com.sorbSoft.CabAcademie.Services.TopicService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TopicService topicService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryViewModel> getCategory(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CategoryViewModel vm = categoryService.getCategoryViewModel(id);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<CategoryViewModel> getCategoryViewModel(){
        CategoryViewModel vm = categoryService.getCategoryViewModel(null);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories= categoryService.fetchAllCategories();
        if(categories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/all/filtered")
    public ResponseEntity<List<Category>> getAllCategoriesFiltered(@RequestParam(value = "categoryId",  required = false) Long categoryId){
        List <Category> categories = categoryService.getAllFiltered(categoryId);
        if(categories == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value="/topics/{id}")
    public ResponseEntity<List<Topic>> getAllTopicsByCategory(@PathVariable Long id){
        List<Topic> topics= topicService.fetchAllTopicByCategory(id);
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

    @PostMapping(value = "/topic")
    public ResponseEntity<List<Topic>> saveTopic(@Valid @RequestBody List<Topic> topics){
        if(!topicService.saveTopics(topics))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/save")
    public  ResponseEntity<String> saveCategory(@Valid @RequestBody CategoryViewModel vm){
        Pair<String, Category> result = categoryService.saveCategory(vm);
        if(result.getValue() == null)
            return new ResponseEntity<>(result.getKey(), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(result.getKey(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id ){
        if(id<0)
            return ResponseEntity.badRequest().build();
        if(categoryService.fetchCategory(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
