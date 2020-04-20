package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.SubCategory;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubCategoryViewModel {
    private Long id;
    @NotNull
    @NotEmpty(message = "Sub-Category name is required")
    private String name;
    @Lob
    private String description;
    private Category category;
    private List<SubCategory> subCategories;
    // for select purpose
    private List<SubCategory> allSubCategories;
    // for select purpose
    private List<Category> allCategories;
}
