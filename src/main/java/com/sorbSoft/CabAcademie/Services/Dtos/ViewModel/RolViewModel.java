package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Rol;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RolViewModel {
    private Long id;
    @Size(min = 2, max = 100)
    private String name;
    @Size(max = 100)
    private String description;
}
