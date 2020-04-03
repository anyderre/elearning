package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by anyderre on 11/08/17.
 */
@Data
@Entity
@Table(name = "user")
@Where(clause = "deleted=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Size(min = 2, max = 100)
    private String firstName;
    @Size(min = 2, max = 100)
    private String lastName;
    @Size(min = 2, max = 100)
    private String email;
    @NotNull
    @Size(min = 4, max = 30)
    @Column(unique=true)
    private String username;
    @Column(name="enabled", nullable = false, columnDefinition = "int default 1")
    private int enable = 1;
    @NotNull(message="Password invalid")
    @Size(max=60)
    private String password;
    public String photoURL;
    @Lob
    public String bio;
    public String country;
    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id")
    private Rol role;
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Course> courses;
    private boolean agreeWithTerms;
    private boolean deleted = false;
}
