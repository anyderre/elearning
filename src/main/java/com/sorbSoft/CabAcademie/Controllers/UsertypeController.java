package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.UserType;
import com.sorbSoft.CabAcademie.Services.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping("/api/userType")
public class UsertypeController {
    @Autowired
    private UserTypeService userTypeService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserType> getUserType(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        UserType userType= userTypeService.fetchUserType(id);
        if(userType==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userType, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserType>> getAllUserType(){
        List<UserType> userTypes= userTypeService.fetchAllUserTypes();
        if(userTypes.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userTypes, HttpStatus.OK);
    }

    @PostMapping()
    public  ResponseEntity<UserType> saveUserType(@Valid @RequestBody UserType userType){
        UserType currentUsertype= userTypeService.saveUserType(userType);
        if(currentUsertype==null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserType> updateUserType(@PathVariable Long id, @RequestBody UserType userType){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userTypeService.fetchUserType(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserType currentUsertype= userTypeService.updateUserType(userType);
        if (currentUsertype==null)
            return  new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>(currentUsertype, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUserType(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(userTypeService.fetchUserType(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userTypeService.deleteUserType(id);
        return new ResponseEntity<>("UserType with "+ id+" Deleted!", HttpStatus.OK);
    }
}
