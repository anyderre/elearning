package com.sorbSoft.CabAcademie.Services.Dtos.ViewModel;

import com.sorbSoft.CabAcademie.Entities.Section;
import lombok.Data;

import java.util.List;

@Data
public class SubSectionViewModel {
    private Long id;
    private String name;
    private String description;
    private Section section;
    // for select purpose
    private List<Section> allSections;
}
