package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.Category;
import com.sorbSoft.CabAcademie.Entities.SubCategory;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubCategoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class SubCategoryFactory {

    public static SubCategoryViewModel getSubCategoryViewModel(){
        return new SubCategoryViewModel() {
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

            @Override
            public Category getCategory() {
                return new Category() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public List<SubCategory> getSubCategories() {
                return new ArrayList<>();
            }
        };
    }
}
