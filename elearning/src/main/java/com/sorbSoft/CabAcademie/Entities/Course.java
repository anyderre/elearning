package com.sorbSoft.CabAcademie.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    private double price;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Syllabus> syllabus;
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id")
    private Section section;
    private boolean isPremium;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private boolean deleted = false;
}
