package com.sorbSoft.CabAcademie.Entities.Factory;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    private static RolServices rolServices;

    @Autowired
    public UserFactory(RolServices rolServices) {
        UserFactory.rolServices = rolServices;
    }

    // private
    public static User mapUser(UserViewModel user){
        return new User() {
            @Override
            public Long getId() {
                return user.getId();
            }

            @Override
            public String getName() {
                return user.getName();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public int getEnable() {
                return 1;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public List<Rol> getRoles() {
                return getUserRoles(user);
            }
        };
    }

//    // private
//    public static User mapUserToVM(User user){
//        return new UserViewModel() {
//            @Override
//            public Long getId() {
//                return user.getId();
//            }
//
//            @Override
//            public String getName() {
//                return user.getName();
//            }
//
//            @Override
//            public String getUsername() {
//                return user.getUsername();
//            }
//
//            @Override
//            public int getEnable() {
//                return 1;
//            }
//
//            @Override
//            public String getPassword() {
//                return user.getPassword();
//            }
//
//        };
//    }

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
            Rol rol = rolServices.getRole(Roles.ROLE_ADMIN.name());
            if (rol != null) {
                roles.add(rol);
            }
        }
        if (user.isAdmin()){
            Rol rol = rolServices.getRole(Roles.ROLE_PROFESSOR.name());
            if (rol != null) {
                roles.add(rol);
            }
        }
        return roles;
    }

}
