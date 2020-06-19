package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorbSoft.CabAcademie.Entities.*;
import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class CourseViewModel {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private float ratings;
    private double price;
    private int enrolled;
    private String author;
    private boolean isPremium;
    private boolean validated;
    private boolean privateOnly;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private Category category;
    private SubCategory subCategory;
    private Section section;
    private SubSection subSection;
    private User user;
    private Overview overview;
    private List<Syllabus> syllabus;
    private List<Objective> objectives;
    private List<User> schools;
    // for select purpose
    private List<Category> categories;
    // for select purpose
    private List<SubCategory> subCategories;
    // for select purpose
    private List<Section> sections;
    // for select purpose
    private List<SubSection> subSections;
    // for select purpose
    private List<User> users;
    // for select purpose
    private List<User> allSchools;

    private List<Video> videos;

    private CourseStatus status;

    private String declineMessage;
}
