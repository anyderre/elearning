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
@Table(name = "category")
@Where(clause = "deleted=false")
public class Category implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
//    private Long parentCategoryId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category parentCategory;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Category> childrenCategory;
    private boolean deleted = false;
}
