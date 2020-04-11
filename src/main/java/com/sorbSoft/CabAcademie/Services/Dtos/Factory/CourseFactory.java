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
            public String getDescription() {
                return "";
            }

            @Override
            public String getImageUrl() {
                return "";
            }

            @Override
            public float getRatings() {
                return 5.0f;
            }

            @Override
            public double getPrice() {
                return 0D;
            }

            @Override
            public int getEnrolled() {
                return 0;
            }

            @Override
            public String getAuthor() {
                return "";
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
            public Section getSection() {
                return new Section(){
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public SubSection getSubSection() {
                return new SubSection(){
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public Category getCategory() {
                return new Category(){
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public SubCategory getSubCategory() {
                return new SubCategory(){
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public User getUser() {

                return new User() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }
                };
            }

            @Override
            public Overview getOverview() {
                return new Overview() {
                    @Override
                    public Long getId() {
                        return 0L;
                    }

                    @Override
                    public List<Requirement> getRequirements() {
                        return new ArrayList<>();
                    }

                    @Override
                    public List<Feature> getFeatures() {
                        return new ArrayList<>();
                    }
                };
            }

            @Override
            public List<Objective> getObjectives() {
                return new ArrayList<>();
            }

            @Override
            public List<Syllabus> getSyllabus() {
                return new  ArrayList<>();
            }

        };
    }
}
