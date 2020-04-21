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
@Table(name = "objective")
@Where(clause = "deleted=false")
public class Objective implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="Objective description cannot be null")
    @NotEmpty(message = "Objective description is required")
    private String description;
    private boolean deleted = false;
}
