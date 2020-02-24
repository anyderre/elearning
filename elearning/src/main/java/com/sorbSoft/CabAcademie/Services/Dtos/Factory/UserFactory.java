package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;

public class UserFactory {
    public static UserViewModel getUserViewModel(){
        return new UserViewModel() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getUsername() {
                return "";
            }

            @Override
            public int getEnable() {
                return 1;
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public boolean isAdmin() {
                return false;
            }
            @Override
            public boolean isProfessor() {
                return false;
            }
        };
    }
}
