package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 100)
    private String name;
    @NotNull
    @Size(min = 4, max = 30)
    @Column(unique=true)
    private String username;
    @Column(name="enabled", nullable = false, columnDefinition = "int default 1")
    private int enable = 1;
    @NotNull(message="Password invalid")
    @Size(max=60)
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Rol> roles;

}
