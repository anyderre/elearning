package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "course")
public class Course implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    private double price;
    @OneToOne(optional = false)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    private boolean premium;
    //duree du cours
    private int duration;
    private Date startDate;

}
