package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Section;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubSectionViewModel {
    private Long id;
    @NotNull
    @NotEmpty(message = "Sub-Section name is required")
    private String name;
    @Lob
    private String description;
    private Section section;
    // for select purpose
    private List<Section> allSections;
}
