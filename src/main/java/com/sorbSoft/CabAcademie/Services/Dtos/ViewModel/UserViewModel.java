package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.*;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Data
public class UserViewModel {
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
    private int enable = 0;
    @NotNull(message="Password invalid")
    @NotEmpty(message = "Password is required")
    @Size(max=60)
    private String password;
    public String photoURL;
    public String bio;
    public String country;
    private boolean agreeWithTerms;
    private List<Category> categories;
    private List<SubCategory> subCategories;
    private Rol role;
    private List<Course> courses;
    private List<User> schools;
    private List<User> organizations;
    // for select purpose
    private List<Rol> allRoles;
    // for select purpose
    private List<User> allSchools;
    // for select purpose
    private List<User> allOrganizations;
    // for select purpose
    private List<Course> allCourses;
    // for select purpose
    private List<Category> allCategories;
    // for select purpose
    private List<SubCategory> allSubCategories;
    private String workspaceName;

    private boolean socialUser;

    private String facebookId;

    private String googleId;

    private String linkedinId;

    private String timeZone;

    private Boolean isDefaultPasswordChanged;

}
