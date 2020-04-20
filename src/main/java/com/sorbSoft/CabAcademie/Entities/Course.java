package com.sorbSoft.CabAcademie.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "course")
@Where(clause = "deleted=false")
public class Course implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty(message = "Title is required")
    private String title;
    @Lob
    private String description;
    @Lob
    private String imageUrl;
    @NotNull
    @Min(value = 0, message = "The rating value cannot be less than zero")
    @Max(value = 5, message = "The rating value cannot more than ")
    private float ratings;
    private int enrolled;
    @NotNull
    @NotEmpty(message = "Author name is required")
    private  String author;
    @Min(value = 0, message = "The price value cannot be less than zero")
    @NotNull
    private double price;
    @NotNull
    private boolean isPremium = true;
    @NotNull
    private boolean validated = false;
    @NotNull
    private boolean privateOnly = false;
    @NotNull
    private boolean deleted = false;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date lastUpdate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="course_syllabus",
            joinColumns = @JoinColumn( name="course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="syllabus_id", referencedColumnName = "id")
    )
    private List<Syllabus> syllabus;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id")
    private Section section;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_section_id")
    private SubSection subSection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy="course", optional = false)
    private Overview overview;
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="course_objectives",
            joinColumns = @JoinColumn( name="course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="objective_id", referencedColumnName = "id")
    )
    private List<Objective> objectives;
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_schools",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> schools;
}