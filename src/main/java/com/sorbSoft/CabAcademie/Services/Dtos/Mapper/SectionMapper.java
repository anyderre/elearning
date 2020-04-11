package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.SectionInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface SectionMapper {
    SectionViewModel mapToViewModel(Section section);

    Section mapToEntity(SectionViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Section mapEntityToEntity(Section section);

    SectionInfo mapEntityToInfo(Section section);
}
