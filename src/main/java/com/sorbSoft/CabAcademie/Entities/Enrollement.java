package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "enrollement")
public class Enrollement implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name="course_id")
    private Course course;
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    private User user;
    private Date enrollementDate;
    private BigDecimal amount;
}
