package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface UserMapper {
    UserViewModel mapToViewModel(User user);

    @Mappings({
//        @Mapping(target="employeeId", source="entity.id"),
//        @Mapping(target="employeeName", source="entity.name")
        @Mapping(target = "enable", ignore = true),
        @Mapping(target = "roles", ignore = true),
    })
    User mapToEntity(UserViewModel vm);


    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    User mapEntityToEntity (User user);

    UserInfo mapEntityToInfo (User user);
}
