package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Data
public class SectionViewModel {
    private Long id;
    @NotNull
    @NotEmpty(message = "Section name is required")
    private String name;
    @Lob
    private String description;
}
