package com.sorbSoft.CabAcademie.Services.Dtos.Mapper;

import com.sorbSoft.CabAcademie.Entities.Course;
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
    date = "2020-04-07T01:20:05-0400",
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
        userViewModel.setFirstName( user.getFirstName() );
        userViewModel.setLastName( user.getLastName() );
        userViewModel.setEmail( user.getEmail() );
        userViewModel.setUsername( user.getUsername() );
        userViewModel.setPassword( user.getPassword() );
        userViewModel.setPhotoURL( user.getPhotoURL() );
        userViewModel.setBio( user.getBio() );
        userViewModel.setCountry( user.getCountry() );
        userViewModel.setAgreeWithTerms( user.isAgreeWithTerms() );
        userViewModel.setSection( user.getSection() );
        userViewModel.setRole( user.getRole() );
        List<Course> list = user.getCourses();
        if ( list != null ) {
            userViewModel.setCourses( new ArrayList<Course>( list ) );
        }

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
        user.setFirstName( vm.getFirstName() );
        user.setLastName( vm.getLastName() );
        user.setEmail( vm.getEmail() );
        user.setUsername( vm.getUsername() );
        user.setPassword( vm.getPassword() );
        user.setPhotoURL( vm.getPhotoURL() );
        user.setBio( vm.getBio() );
        user.setCountry( vm.getCountry() );
        user.setRole( vm.getRole() );
        user.setSection( vm.getSection() );
        List<Course> list = vm.getCourses();
        if ( list != null ) {
            user.setCourses( new ArrayList<Course>( list ) );
        }
        user.setAgreeWithTerms( vm.isAgreeWithTerms() );

        return user;
    }

    @Override
    public User mapEntityToEntity(User user) {
        if ( user == null ) {
            return null;
        }

        User user1 = new User();

        user1.setName( user.getName() );
        user1.setFirstName( user.getFirstName() );
        user1.setLastName( user.getLastName() );
        user1.setEmail( user.getEmail() );
        user1.setUsername( user.getUsername() );
        user1.setEnable( user.getEnable() );
        user1.setPassword( user.getPassword() );
        user1.setPhotoURL( user.getPhotoURL() );
        user1.setBio( user.getBio() );
        user1.setCountry( user.getCountry() );
        user1.setRole( user.getRole() );
        user1.setSection( user.getSection() );
        List<Course> list = user.getCourses();
        if ( list != null ) {
            user1.setCourses( new ArrayList<Course>( list ) );
        }
        user1.setAgreeWithTerms( user.isAgreeWithTerms() );
        user1.setDeleted( user.isDeleted() );

        return user1;
    }

    @Override
    public UserInfo mapEntityToInfo(User user) {
        if ( user == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setRoleName( userRoleName( user ) );
        userInfo.setId( user.getId() );
        userInfo.setFirstName( user.getFirstName() );
        userInfo.setLastName( user.getLastName() );
        userInfo.setEmail( user.getEmail() );
        userInfo.setUsername( user.getUsername() );

        return userInfo;
    }

    private String userRoleName(User user) {
        if ( user == null ) {
            return null;
        }
        Rol role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
