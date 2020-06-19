package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import lombok.Data;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RolViewModel {
    private Long id;
    @NotNull(message="Role name cannot be null")
    @NotEmpty(message = "Role name is is required")
    private String name;
    @Lob
    private String description;
    private boolean generated;

    private Roles role;
}
