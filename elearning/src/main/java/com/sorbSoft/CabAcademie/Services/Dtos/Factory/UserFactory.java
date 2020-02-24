package com.sorbSoft.CabAcademie.Services.Dtos.Factory;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
@Component
public class UserFactory {

    private static RolServices rolServices;

    @Autowired
    public UserFactory(RolServices rolServices) {
        UserFactory.rolServices = rolServices;
    }

//    // private
//    public static User mapUser(UserViewModel user){
//        User resUser = new User();
//        resUser.setName(user.getName());
//        resUser.setUsername(user.getUsername());
//        resUser.setEnable(1);
//        resUser.setDeleted(false);
//        resUser.setPassword(user.getPassword());
//        resUser.setId(user.getId());
//        resUser.setRoles(rolServices.getUserRoles(user));
//        return resUser;
//    }
//
//    // private
//    public static UserViewModel mapUserToVM(User user){
//        Pair<Boolean, Boolean> roles = rolServices.getUserRoles(user);
//        UserViewModel resUser = new UserViewModel();
//        resUser.setName(user.getName());
//        resUser.setUsername(user.getUsername());
//        resUser.setEnable(1);
//        resUser.setDeleted(user.isDeleted());
//        resUser.setId(user.getId());
//        resUser.setPassword(user.getPassword());
//        resUser.setAdmin(roles.getFirst());
//        resUser.setProfessor(roles.getSecond());
//        return resUser;
//    }

    public static UserViewModel getUserViewModel(){
        return new UserViewModel() {
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


}
