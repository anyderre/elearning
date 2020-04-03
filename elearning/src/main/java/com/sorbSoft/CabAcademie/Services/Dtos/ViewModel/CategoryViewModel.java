package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Entities.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CategoryViewModel {
    private Long id;
    private String name;
    private String description;
    private Category parentCategory;
    private List<Category> childrenCategory;
    // for select purpose
    private List<Category> categories;
    private boolean deleted;
}
