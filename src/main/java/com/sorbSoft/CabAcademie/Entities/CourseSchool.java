package com.sorbSoft.CabAcademie.Entities;

import com.sorbSoft.CabAcademie.Entities.Enums.CourseStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name ="course_schools_new")
@Data
public class CourseSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private User school;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User teacher;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    private String declineMessage;
}
