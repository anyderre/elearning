package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-03-06T21:46:03-0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserViewModel mapToViewModel(User user) {
        if ( user == null ) {
            return null;
        }

        UserViewModel userViewModel = new UserViewModel();

        userViewModel.setId( user.getId() );
        userViewModel.setName( user.getName() );
        userViewModel.setUsername( user.getUsername() );
        userViewModel.setEnable( user.getEnable() );
        userViewModel.setPassword( user.getPassword() );
        List<Rol> list = user.getRoles();
        if ( list != null ) {
            userViewModel.setRoles( new ArrayList<Rol>( list ) );
        }
        userViewModel.setDeleted( user.isDeleted() );

        return userViewModel;
    }

    @Override
    public User mapToEntity(UserViewModel vm) {
        if ( vm == null ) {
            return null;
        }

        User user = new User();

        user.setId( vm.getId() );
        user.setName( vm.getName() );
        user.setUsername( vm.getUsername() );
        user.setPassword( vm.getPassword() );
        user.setDeleted( vm.isDeleted() );

        return user;
    }

    @Override
    public User mapEntityToEntity(User user) {
        if ( user == null ) {
            return null;
        }

        User user1 = new User();

        user1.setName( user.getName() );
        user1.setUsername( user.getUsername() );
        user1.setEnable( user.getEnable() );
        user1.setPassword( user.getPassword() );
        List<Rol> list = user.getRoles();
        if ( list != null ) {
            user1.setRoles( new ArrayList<Rol>( list ) );
        }
        user1.setDeleted( user.isDeleted() );

        return user1;
    }

    @Override
    public UserInfo mapEntityToInfo(User user) {
        if ( user == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setId( user.getId() );
        userInfo.setName( user.getName() );
        userInfo.setUsername( user.getUsername() );

        return userInfo;
    }
}
