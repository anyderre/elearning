package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "syllabus")
public class Syllabus implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
}
