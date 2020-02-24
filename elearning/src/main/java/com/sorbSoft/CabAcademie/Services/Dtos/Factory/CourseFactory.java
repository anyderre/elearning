package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.CourseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CourseFactory {

    public static CourseViewModel getCourseViewModel(){
        return new CourseViewModel() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getTitle() {
                return "";
            }

            @Override
            public User getUser() {
                return null;
            }

            @Override
            public double getPrice() {
                return 0D;
            }

            @Override
            public List<Syllabus> getSyllabus() {
                return new ArrayList<>();
            }

            @Override
            public Category getCategory() {
                return new Category(){
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
            @Override
            public boolean isPremium() {
                return true;
            }

            @Override
            public Date getStartDate() {
                return new Date();
            }

            @Override
            public Date getEndDate() {
                return new Date();
            }

            @Override
            public boolean isDeleted() {
                return false;
            }

            @Override
            public Section getSection() {
                return new Section(){
                    @Override
                    public Long getId() {
                        return 0L;
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }

                    @Override
                    public String getName() {
                        return "";
                    }
                };
            }
        };
    }

}
