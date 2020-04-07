package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.SubCategory;
import lombok.Data;

import java.util.List;

@Data
public class SubCategoryViewModel {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private List<SubCategory> subCategories;
    // for select purpose
    private List<SubCategory> allSubCategories;
    private List<Category> allCategories;
}
