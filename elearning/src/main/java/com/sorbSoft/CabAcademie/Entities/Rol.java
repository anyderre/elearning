package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private String name;
    private String description;
    private boolean deleted = false;
    private boolean enabled = true;
}