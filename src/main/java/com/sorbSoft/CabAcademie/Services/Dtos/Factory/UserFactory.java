package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

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
            public String getBio() {
                return "";
            }

            @Override
            public String getPhotoURL() {
                return "";
            }

            @Override
            public String getCountry() {
                return "";
            }

            @Override
            public List<Category> getCategories() {
                return new ArrayList<>();
            }

            @Override
            public List<SubCategory> getSubCategories() {
                return new ArrayList<>();
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

            @Override
            public List<Course> getCourses() {
                return new ArrayList<>();
            }

            @Override
            public List<User> getOrganizations() {
                return new ArrayList<>();
            }

            @Override
            public List<User> getSchools() {
                return new ArrayList<>();
            }
        };
    }
}
