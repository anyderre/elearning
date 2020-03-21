package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Factory.UserFactory;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Mapper.UserMapper;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anyderre on 11/08/17.
 */
@Service
@Transactional
public class UserServices {
    @Autowired
   private UserRepository UserRepository;
    @Autowired
    private RolServices rolServices;
    @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    private UserMapper mapper
            = Mappers.getMapper(UserMapper.class);

    public Pair<String, User> saveUser(UserViewModel vm){
        if (vm.getId() > 0L) {
            return updateUser(vm);
        } else {
            User savedUser = userRepository.findByUsername(vm.getUsername());

            if ( savedUser != null){
                return Pair.of("The user you are trying to save already exist", null);
            }
            User resultUser = mapper.mapToEntity(vm);
            resultUser.setPassword(bCryptPasswordEncoder.encode(resultUser.getPassword()));
//            resultUser.setRole(rolServices.getUserRoles(vm));
            User result = userRepository.save(resultUser);
            if (result == null){
                return Pair.of("Couldn't save the user", null);
            } else {
                return  Pair.of("User saved successfully", result);
            }
        }
    }

    public Pair<String, User> updateUser(UserViewModel vm){
        User savedUser = userRepository.findUserByUsernameAndIdIsNot(vm.getUsername(), vm.getId());
        if (savedUser != null) {
            return Pair.of("The user name already exist for another definition", null);
        }
        User currentUser = UserRepository.findById(vm.getId());

        if (currentUser == null){
            return Pair.of("The user you are trying to update does not esxist anymore", null);
        }

        User resultUser = mapper.mapToEntity(vm);
//        resultUser.setRoles(rolServices.getUserRoles(vm));
        User result = userRepository.save(resultUser);

        if (result == null) {
            return Pair.of("Couldn't update the user", null);
        } else {
            return Pair.of("User updated successfully", result);
        }
    }

    public User findUserbyUsername(String username){
        return UserRepository.findByUsername(username);
    }

    public List<User> findAllUsersFilterd(String filter){
        List<User> collect = UserRepository.findAll().stream().filter(user ->
                !user.getRole().equals(filter)).collect(Collectors.toList());
        return collect;
    }

    public String deleteUser(Long id){
        if (id <= 0L) {
            return "You should indicate the id of the user";
        }
        User user = userRepository.findById(id);
        if (user == null) {
            return "The user you want to delete doesn't exist";
        }
        user.setDeleted(true);
        return "User successfully deleted";
    }

    public User findAUser(Long id){
        return UserRepository.findById(id);
    }

    public UserViewModel getUserViewModel(Long userId){
        UserViewModel vm = UserFactory.getUserViewModel();
        if (userId != null && userId > 0) {
            User user = this.findAUser(userId);
            if (user == null) {
                return null;
            } else {
                vm = mapper.mapToViewModel(user);
//                Pair<Boolean, Boolean> roles = rolServices.getUserRoles(user);
//                vm.setAdmin(roles.getFirst());
//                vm.setProfessor(roles.getSecond());
                vm.setAllRoles(rolServices.todosRoles());
            }
            return vm;
        }
        return vm;
    }

    public List <UserInfo> getUserInfo(){
        List<User> users= this.findAllUsersFilterd(Roles.ROLE_SUPER_ADMIN.name());
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        List<UserInfo> info = new ArrayList<>();
        for (User user : users) {
            UserInfo uInfo = mapper.mapEntityToInfo(user);
            if (user.getRole() != null) {
                uInfo.setRoleName(user.getRole().getName());
            }
            info.add(uInfo);
        }
        return info;
    }
}

