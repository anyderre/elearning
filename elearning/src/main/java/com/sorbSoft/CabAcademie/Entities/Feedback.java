package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "feedback")
public class Feedback implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name="enrollement_id")
    private Enrollement enrollement;
    @ManyToOne(optional = false)
    @JoinColumn(name="course_id")
    private Course course;
    private String comment;
    private float rating;
}
