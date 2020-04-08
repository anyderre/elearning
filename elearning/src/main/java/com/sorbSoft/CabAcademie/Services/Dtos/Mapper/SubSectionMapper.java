package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.SubSection;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SubSectionViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface SubSectionMapper {
    @Mappings({
            @Mapping(target = "allSections", ignore = true),
    })
    SubSectionViewModel mapToViewModel(SubSection section);

    @Mappings({
            @Mapping(target = "section", ignore = true),
            @Mapping(target = "deleted", ignore = true),
    })
    SubSection mapToEntity(SubSectionViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    SubSection mapEntityToEntity(SubSection section);
}
