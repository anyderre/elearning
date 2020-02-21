package com.sorbSoft.CabAcademie.Entities.Factory;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Repository.RolRepository;
import com.sorbSoft.CabAcademie.Services.RolServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
@Component
public class UserFactory {

    private static RolRepository rolRepository;
    private static ModelMapper mapper;

    @Autowired
    public UserFactory(RolRepository rolRepository) {
        UserFactory.rolRepository = rolRepository;
    }

    // private
    public static User mapUser(UserViewModel user){
        User resUser = new User();
        resUser.setName(user.getName());
        resUser.setUsername(user.getUsername());
        resUser.setEnable(1);
        resUser.setDeleted(false);
        resUser.setPassword(user.getPassword());
        resUser.setId(user.getId());
        resUser.setRoles(getUserRoles(user));
        return resUser;
    }


    // private
    public static UserViewModel mapUserToVM(User user){
        Pair<Boolean, Boolean> roles = getUserRoles(user);
        UserViewModel resUser = new UserViewModel();
        resUser.setName(user.getName());
        resUser.setUsername(user.getUsername());
        resUser.setEnable(1);
        resUser.setDeleted(user.isDeleted());
        resUser.setId(user.getId());
        resUser.setPassword(user.getPassword());
        resUser.setAdmin(roles.getFirst());
        resUser.setProfessor(roles.getSecond());
        return resUser;
    }

    public static User getUserViewModel(){
        return new User() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public String getUsername() {
                return "";
            }

            @Override
            public int getEnable() {
                return 1;
            }

            @Override
            public String getPassword() {
                return "";
            }
        };
    }

    private static List<Rol> getUserRoles (UserViewModel user) {
        List<Rol> roles = new ArrayList<>();
        if (user.isAdmin()){
            Rol rol = rolRepository.findAllByRol(Roles.ROLE_ADMIN.name());
            if (rol != null) {
                roles.add(rol);
            }
        }
        if (user.isProfessor()){
            Rol rol = rolRepository.findAllByRol(Roles.ROLE_PROFESSOR.name());
            if (rol != null) {
                roles.add(rol);
            }
        }
        return roles;
    }

    private static Pair<Boolean, Boolean> getUserRoles (User user) {
        if (user.getRoles().isEmpty()){
            return Pair.of(false, false);
        }
        boolean admin = false;
        boolean prof = false;
        for (Rol rol : user.getRoles()) {
            if (rol.getRol().equals(Roles.ROLE_ADMIN.name())){
                admin = true;
            } else if (rol.getRol().equals(Roles.ROLE_PROFESSOR.name())){
                prof = true;
            }
        }
        return Pair.of(admin, prof);
    }

}
