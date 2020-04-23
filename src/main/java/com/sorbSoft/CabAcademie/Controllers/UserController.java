package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserServices userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserViewModel> getUser(@PathVariable Long id, @RequestParam (value = "filterRoles", defaultValue = "1") String filterRoles){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        UserViewModel vm= userService.getUserViewModel(id, filterRoles);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserViewModel> getCourseViewModel(@RequestParam (value = "filterRoles", defaultValue = "1")  String filterRoles){
        UserViewModel vm = userService.getUserViewModel(null, filterRoles);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/loggedIn")
    public ResponseEntity<User> getLoggedInUser(Principal user){
        User loadedUser= userService.findUserbyUsername(user.getName());
        return new ResponseEntity<>(loadedUser, HttpStatus.OK);
    }

    @GetMapping(value = "/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users= userService.findAllUser();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/professors" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> findAllProfessors(){
        List<User> users= userService.findAllProfessor();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/professor/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findProfessor(@PathVariable Long id){
        User professor = userService.findProfessor(id);
        if(professor == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(professor, HttpStatus.OK);
    }

    @GetMapping(value = "/info" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserInfo>> findUserInfo(){
        List<UserInfo> users= userService.getUserInfo();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/save")
    public  ResponseEntity<MessageResponse> saveUser(@Valid @RequestBody UserViewModel user){
        if (userRepository.existsByUsernameAndIdIsNot(user.getUsername(), user.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.of("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmailAndIdIsNot(user.getEmail(), user.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.of("Error: Email is already in use!"));
        }

        Result result = userService.saveUser(user);
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("User successfully created"), HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id ){
        Result result =  userService.deleteUser(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Section successfully deleted"), HttpStatus.OK);
    }
}
