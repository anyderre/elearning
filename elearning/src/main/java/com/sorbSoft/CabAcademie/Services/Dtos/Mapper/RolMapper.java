package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.RolInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.RolViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface RolMapper {
    RolViewModel mapToViewModel(Rol user);

    @Mappings({
        @Mapping(target = "enabled", ignore = true),
        @Mapping(target = "deleted", ignore = true),
    })
    Rol mapToEntity(RolViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Rol mapEntityToEntity(Rol role);

    RolInfo mapEntityToInfo(Rol role);
}
