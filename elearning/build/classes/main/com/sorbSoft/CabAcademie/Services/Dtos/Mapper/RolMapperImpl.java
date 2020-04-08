package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.RolInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.RolViewModel;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-08T01:55:22-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class RolMapperImpl implements RolMapper {

    @Override
    public RolViewModel mapToViewModel(Rol user) {
        if ( user == null ) {
            return null;
        }

        RolViewModel rolViewModel = new RolViewModel();

        rolViewModel.setId( user.getId() );
        rolViewModel.setName( user.getName() );
        rolViewModel.setDescription( user.getDescription() );

        return rolViewModel;
    }

    @Override
    public Rol mapToEntity(RolViewModel vm) {
        if ( vm == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setId( vm.getId() );
        rol.setName( vm.getName() );
        rol.setDescription( vm.getDescription() );

        return rol;
    }

    @Override
    public Rol mapEntityToEntity(Rol role) {
        if ( role == null ) {
            return null;
        }

        Rol rol = new Rol();

        rol.setName( role.getName() );
        rol.setDescription( role.getDescription() );
        rol.setDeleted( role.isDeleted() );
        rol.setEnabled( role.isEnabled() );

        return rol;
    }

    @Override
    public RolInfo mapEntityToInfo(Rol role) {
        if ( role == null ) {
            return null;
        }

        RolInfo rolInfo = new RolInfo();

        rolInfo.setId( role.getId() );
        rolInfo.setName( role.getName() );
        rolInfo.setDescription( role.getDescription() );

        return rolInfo;
    }
}
