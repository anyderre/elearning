package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "syllabus")
@Where(clause = "deleted = false")
public class Syllabus implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chapterTitle;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Video> chapterTuts;
    private boolean deleted = false;
}
