package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Section;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SectionViewModel;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-03T00:34:16-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class SectionMapperImpl implements SectionMapper {

    @Override
    public SectionViewModel mapToViewModel(Section section) {
        if ( section == null ) {
            return null;
        }

        SectionViewModel sectionViewModel = new SectionViewModel();

        sectionViewModel.setId( section.getId() );
        sectionViewModel.setDescription( section.getDescription() );
        sectionViewModel.setName( section.getName() );
        sectionViewModel.setDeleted( section.isDeleted() );

        return sectionViewModel;
    }

    @Override
    public Section mapToEntity(SectionViewModel vm) {
        if ( vm == null ) {
            return null;
        }

        Section section = new Section();

        section.setId( vm.getId() );
        section.setDescription( vm.getDescription() );
        section.setName( vm.getName() );
        section.setDeleted( vm.isDeleted() );

        return section;
    }

    @Override
    public Section mapEntityToEntity(Section section) {
        if ( section == null ) {
            return null;
        }

        Section section1 = new Section();

        section1.setDescription( section.getDescription() );
        section1.setName( section.getName() );
        section1.setDeleted( section.isDeleted() );

        return section1;
    }
}
