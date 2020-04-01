package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sorbSoft.CabAcademie.Entities.*;
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
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private Section section;
    private Category category;
    private User user;
    private Overview overview;
    private List<Syllabus> syllabus;
    private List<Objective> objectives;
    private List<Category> categories;
    private List<Section> sections;
    private List<User> users;
}
