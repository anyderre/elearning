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
    @Lob
    private String description;
    @Size(min = 1 )
    private String name;
    private boolean deleted = false;
}
