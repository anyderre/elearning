package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Services.CategoryService;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.CategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CategoryViewModel> getCategory(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CategoryViewModel vm = categoryService.getCategoryViewModel(id);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CategoryViewModel> getCategoryViewModel(){
        CategoryViewModel vm = categoryService.getCategoryViewModel(null);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CategoryInfo>> getAllCategories(){
        List<CategoryInfo> categories= categoryService.getUserInfo();
        if(categories.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/info" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryInfo>> getAllCategoriesInfo(){
        List<CategoryInfo> categories = categoryService.getCategoryInfo();
        if(categories.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/all/filtered")
    public ResponseEntity<List<Category>> getAllCategoriesFiltered(@RequestParam(value = "categoryId",  required = false) Long categoryId){
        List <Category> categories = categoryService.getAllFiltered(categoryId);
        if(categories == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity<MessageResponse> saveCategory(@Valid @RequestBody CategoryViewModel vm){
        Result result = categoryService.saveCategory(vm);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("Category successfully added"), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long id ){
        Result result = categoryService.deleteCategory(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Sub-Category successfully deleted"), HttpStatus.OK);
    }
}
