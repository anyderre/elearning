package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "video")
public class Video implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @OneToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;
    private Long previous;
    private Long next;
    private boolean premium;
}
