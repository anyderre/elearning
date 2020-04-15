package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class SectionViewModel {
    private Long id;
    @Size(max=250, message = "The description can't be longer than 250 characters")
    private String description;
    @Size(max=50, message = "The name can't be longer than 250 characters")
    private String name;
}
