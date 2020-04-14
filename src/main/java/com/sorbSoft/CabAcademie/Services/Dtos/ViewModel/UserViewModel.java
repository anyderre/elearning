package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.*;
import lombok.Data;

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
    private String firstName;
    private String lastName;
    private String email;
    private String username;
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
}
