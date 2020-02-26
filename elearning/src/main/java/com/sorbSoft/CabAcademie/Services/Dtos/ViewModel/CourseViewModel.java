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
    private User user;
    private double price;
    private Category category;
    private Section section;
    private boolean isPremium;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private List<Syllabus> syllabus;
    private List<Category> categories;
    private List<Section> sections;
    private List<User> users;
    private boolean deleted;
}
