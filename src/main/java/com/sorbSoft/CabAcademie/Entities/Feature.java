package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "feature")
@Where(clause = "deleted=false")
public class Feature implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="Feature title cannot be null")
    @NotEmpty(message = "Feature title is required")
    private String title;
    @NotNull
    private String icon;
    private boolean deleted = false;
}
