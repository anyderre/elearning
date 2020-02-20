package com.sorbSoft.CabAcademie.Entities.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Rol;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Data
public class UserViewModel {
    private Long id;
    @Size(min = 2, max = 100)
    private String name;
    @NotNull
    @Size(min = 4, max = 30)
    private String username;
    private int enable;
    @NotNull(message="Password invalid")
    @Size(max=60)
    private String password;
    private List<Rol> roles;
    private boolean deleted;
    private boolean professor;
    private boolean admin;

}
