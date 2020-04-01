package com.sorbSoft.CabAcademie.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    private String title;
    private String description;
    private String imageUrl;
    private float ratings;
    private int enrolled;
    private  String author;
    private double price;
    private boolean isPremium;
    private boolean deleted = false;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date lastUpdate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Syllabus> syllabus;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id")
    private Section section;
    @OneToOne(cascade = CascadeType.ALL, mappedBy="course", optional = false)
    private Overview overview;
//    @ElementCollection
////    @CollectionTable(
////            name="objectives",
////            joinColumns=@JoinColumn(name="objectives_ID")
////    )
////    @Column(name="description")
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Objective> objectives;
}