package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;

public class SectionFactory {
    public static SectionViewModel getSectionViewModel(){
        return new SectionViewModel() {
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
