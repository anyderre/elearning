package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Rol;
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
    @Size(min = 2, max = 100)
    private String firstName;
    @Size(min = 2, max = 100)
    private String lastName;
    @Size(min = 2, max = 100)
    private String email;
    @NotNull
    @Size(min = 4, max = 30)
    private String username;
    @NotNull(message="Password invalid")
    @Size(max=60)
    private String password;
    private Rol role;
    private List<Rol> allRoles;
    private boolean agreeWithTerms;
}
