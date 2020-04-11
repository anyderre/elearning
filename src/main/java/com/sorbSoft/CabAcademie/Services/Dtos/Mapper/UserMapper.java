package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "allRoles", ignore = true),
            @Mapping(target = "allCourses", ignore = true),
            @Mapping(target = "allSchools", ignore = true),
            @Mapping(target = "allOrganizations", ignore = true),
            @Mapping(target = "sections", ignore = true),
    })
    UserViewModel mapToViewModel(User user);

    @Mappings({
        @Mapping(target = "enable", ignore = true),
        @Mapping(target = "deleted", ignore = true),
    })
    User mapToEntity(UserViewModel vm);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    User mapEntityToEntity (User user);

    @Mappings({
            @Mapping(target = "roleName",  source = "role.name"),
    })
    UserInfo mapEntityToInfo (User user);
}
