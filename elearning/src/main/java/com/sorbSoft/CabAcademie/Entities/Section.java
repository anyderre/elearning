package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "section")
@Where(clause = "deleted = false")
public class Section implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max=250, message = "The description can't be longer than 250 characters")
    private String description;
    @Size(max=50, message = "The name can't be longer than 250 characters")
    private String name;
    private boolean deleted = false;
}
