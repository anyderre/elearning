package com.sorbSoft.CabAcademie.Services;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public Result validateUserExists(Long userId) {
        Result result = new Result();
        User user = userRepository.getOne(userId);
        if (user == null) {
            result.add("User with " + userId + " doesn't exist");
            return result;
        }
        return result;
    }
}
