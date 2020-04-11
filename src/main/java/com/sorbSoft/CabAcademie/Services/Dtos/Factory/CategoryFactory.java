package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.*;
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
            public String getName() {
                return "";
            }

            @Override
            public String getDescription() {
                return "";
            }

        };
    }
}
