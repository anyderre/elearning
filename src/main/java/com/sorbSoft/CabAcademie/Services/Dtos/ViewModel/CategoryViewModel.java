package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;


@Data
public class CategoryViewModel {
    private Long id;
    @NotNull
    @NotEmpty(message = "Category name is required")
    private String name;
    @Lob
    private String description;
}
