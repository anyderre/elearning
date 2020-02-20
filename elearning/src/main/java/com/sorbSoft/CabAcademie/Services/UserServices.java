package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Factory.UserFactory;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Entities.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
   private UserServices UserServices;
    
    @Autowired
    private UserRepository userRepository;

    public Pair<String, User> saveUser(UserViewModel user){
        User resultUser = UserFactory.mapUser(user);
        if (user.getId() > 0L) {
            return updateUser(resultUser);
        } else {
            User savedUser = userRepository.findByUsername(user.getUsername());

            if ( savedUser != null){
                return new Pair<>("The user you are trying to save already exist", null);
            }
            User result = userRepository.save(resultUser);
            if (result == null){
                return new Pair<>("Couldn't save the user", null);
            } else {
                return  new Pair<>("User saved successfully", result);
            }
        }
    }

    public Pair<String, User> updateUser(User user){
        User savedUser = userRepository.findUserByUsernameAndIdIsNot(user.getUsername(), user.getId());
        if (savedUser != null) {
            return new Pair<>("The user name already exist for another definition", null);
        }
        User currentUser = UserRepository.findById(user.getId());
        currentUser.setEnable(user.getEnable());
        currentUser.setName(user.getName());
        currentUser.setUsername(user.getUsername());
        currentUser.setRoles(user.getRoles());

        User result = userRepository.save(currentUser);
        if (result == null) {
            return new Pair<>("Couldn't update the user", null);
        } else {
            return new Pair<>("User updated successfully", result);
        }
    }

    public User findUserbyUsername(String username){
        return UserRepository.findByUsername(username);
    }

    public List<User> findUsers(int page, int itemPerPage){
        Pageable pageable = new PageRequest(page,itemPerPage);
        return UserRepository.findAll(pageable);
    }

    public List<User> findAllUsers(){
        return UserRepository.findAll();
    }

    public List<User> findUserByUsername(String username){
        return UserRepository.findAllByUsername(username);
    }

    public void eliminarUserPorId(Long id){
        UserRepository.deleteById(id);
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

}

