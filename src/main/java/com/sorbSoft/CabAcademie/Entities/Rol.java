package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Dany on 07/09/2017.
 */
@Data
@NoArgsConstructor
@Entity
@Where(clause = "deleted=false")
public class Rol implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="Role name cannot be null")
    @NotEmpty(message = "Role name is is required")
    private String name;
    @NotNull
    private String description;
    private boolean generated = true;
    private boolean deleted = false;
    private boolean enabled = true;
}