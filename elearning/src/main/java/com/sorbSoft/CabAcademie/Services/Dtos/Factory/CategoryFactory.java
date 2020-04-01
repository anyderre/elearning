package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Entities.Syllabus;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CategoryViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CategoryFactory {

    public static CategoryViewModel getCategoryViewModel(){
        return new CategoryViewModel() {
            @Override
            public Long getId() {
                return 0L;
            }
            @Override
            public boolean isDeleted() {
                return false;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public Category getParentCategory() {
                return new Category() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }
        };
    }
}
