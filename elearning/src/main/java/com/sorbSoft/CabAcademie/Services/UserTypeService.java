package com.sorbSoft.CabAcademie.Services;


import com.sorbSoft.CabAcademie.Entities.UserType;
import com.sorbSoft.CabAcademie.Repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dany on 18/05/2018.
 */
@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository userTypeRepository;

    public List<UserType> fetchAllUserTypes(){
        return userTypeRepository.findAll();
    }

    public UserType fetchUserType(Long id){
        return userTypeRepository.findOne(id);
    }

    public UserType updateUserType(UserType userType){
        UserType currentUserType= userTypeRepository.findOne(userType.getId());
        currentUserType.setType(userType.getType());
        currentUserType.setUser(userType.getUser());

        return userTypeRepository.save(currentUserType);
    }
    public UserType saveUserType(UserType userType){
        return userTypeRepository.save(userType);
    }
    public void deleteUserType(Long id){
        userTypeRepository.delete(id);
    }
    //other delete methods
    //other fetching methods

}
