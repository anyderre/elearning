package com.sorbSoft.CabAcademie.Controladores;

import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user= userService.findAUser(id);
        if(user==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/loggedIn")
    public ResponseEntity<User> getLoggedInUser(Principal user){
        System.out.println("Printing user");
        System.out.println(user.getName());
        User loadedUser= userService.findUserbyUsername(user.getName());

        return new ResponseEntity<>(loadedUser, HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users= userService.findAllUsers();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<User> saveUsers(@Valid @RequestBody User user){
        User currentUser= userService.saveUser(user);
        if(currentUser==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.findAUser(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User currentUser= userService.updateUser(user);
        if (currentUser==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userService.findAUser(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.deleteUser(id);
        return new ResponseEntity<>("User with "+ id+" Deleted!", HttpStatus.OK);
    }
}
