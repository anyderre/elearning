package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by Dany on 13/05/2018.
 */
@Entity
@Data
@Table(name = "overview")
@Where(clause = "deleted=false")
public class Overview implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "overview_requirements",
            joinColumns = @JoinColumn(name = "overview_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "requirement_id", referencedColumnName = "id"))
    private List<Requirement> requirements;
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "overview_features",
            joinColumns = @JoinColumn(name = "overview_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id"))
    private List<Feature> features;
    private boolean deleted = false;
}
