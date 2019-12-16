package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dany on 15/05/2018.
 */
@Entity
@Data
@Table(name = "type")
public class Type implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
}
