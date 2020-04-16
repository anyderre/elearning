package com.sorbSoft.CabAcademie.Entities;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by anyderre on 11/08/17.
 */
@Data
@Entity
@Table(	name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Where(clause = "deleted=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    @Column(unique=true)
    private String email;
    @NotNull
    @NotEmpty(message = "UserName is required")
    @Size(min = 4, max = 30)
    private String username;
    @Column(name="enabled", nullable = false, columnDefinition = "int default 1")
    private int enable = 1;
    @NotNull(message="Password invalid")
    @NotEmpty(message = "Password is required")
    @Size(max=60)
    private String password;
    @Lob
    private String photoURL;
    @Lob
    private String bio;
    private String country;
    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id")
    @NotNull
    private Rol role;
    @Fetch(value = FetchMode.SUBSELECT)
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name ="user_id")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_categories",
            joinColumns = @JoinColumn( name="category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName = "id")
    )
    private List<Category> categories;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_sub_categories",
            joinColumns = @JoinColumn( name="sub_category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName = "id")
    )
    private List<SubCategory> subCategories;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_courses",
            joinColumns = @JoinColumn( name="course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName = "id")
    )
    private List<Course> courses;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_schools",
//            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName = "id")
            uniqueConstraints=@UniqueConstraint(columnNames={"user_id"})
    )
    private List<User> schools;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_organizations",
//            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName = "id")
            uniqueConstraints=@UniqueConstraint(columnNames={"user_id"})
    )
    private List<User> organizations;
    private boolean agreeWithTerms;
    private boolean deleted = false;

    private String workspaceName;
}
