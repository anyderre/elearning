package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.Course;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Entities.User;
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
            public Section getSection() {
                return new Section() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
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

//            @Override
//            public List<Course> getAllCourses() {
//                return new ArrayList<>();
//            }

//            @Override
//            public List<User> getSchools() {
//                return new ArrayList<>();
//            }

//            @Override
//            public List<Section> getSections() {
//                return new ArrayList<>();
//            }
//
//            @Override
//            public List<Rol> getAllRoles() {
//                return new ArrayList<>();
//            }
//
//            @Override
//            public List<User> getAllSchools() {
//                return new ArrayList<>();
//            }
        };
    }
}
