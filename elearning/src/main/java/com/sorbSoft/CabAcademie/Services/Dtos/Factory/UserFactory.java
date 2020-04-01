package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;

public class UserFactory {
    public static UserViewModel getUserViewModel(){
        return new UserViewModel() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getLastName() {
                return "";
            }

            @Override
            public String getEmail() {
                return "";
            }

            @Override
            public Rol getRole() {
                return new Rol() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public String getUsername() {
                return "";
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public boolean isAgreeWithTerms() {
                return false;
            }
        };
    }
}
