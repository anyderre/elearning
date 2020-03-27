package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.RolViewModel;

public class RolFactory {
    public static RolViewModel getRolViewModel(){
        return new RolViewModel() {
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
