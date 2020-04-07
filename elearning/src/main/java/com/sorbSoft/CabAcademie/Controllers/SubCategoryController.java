package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.SubCategory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SubCategoryInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubCategoryViewModel;
import com.sorbSoft.CabAcademie.Services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping(value = "/api/subCategory")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubCategoryViewModel> getSubCategory(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SubCategoryViewModel vm = subCategoryService.getSubCategoryViewModel(id);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<SubCategoryViewModel> getSubCategoryViewModel(){
        SubCategoryViewModel vm = subCategoryService.getSubCategoryViewModel(null);
        if(vm == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<List<SubCategoryInfo>> getAllSubCategoriesInfo(){
        List<SubCategoryInfo> subCategories= subCategoryService.getSubCategoryInfo();
        if(subCategories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<SubCategory>> getAllSubCategories(){
        List<SubCategory> subCategories= subCategoryService.fetchAllSubCategories();
        if(subCategories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @GetMapping(value = "/all/filtered")
    public ResponseEntity<List<SubCategory>> getAllSubCategoriesFiltered(@RequestParam(value = "subCategoryId",  required = false) Long categoryId){
        List <SubCategory> subCategories = subCategoryService.getAllFiltered(categoryId);
        if(subCategories == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity<String> saveSubCategory(@Valid @RequestBody SubCategoryViewModel vm){
        Pair<String, SubCategory> result = subCategoryService.saveSubCategory(vm);
        if(result.getSecond() == null)
            return new ResponseEntity<>(result.getFirst(), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(result.getFirst(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long id ){
        if(id<0)
            return ResponseEntity.badRequest().build();
        if(subCategoryService.fetchSubCategory(id)==null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(subCategoryService.deleteSubCategory(id));
    }
}
