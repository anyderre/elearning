package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.EmptyValueException;
import com.sorbSoft.CabAcademie.exception.PasswordsDoNotMatchException;
import com.sorbSoft.CabAcademie.exception.UserNotFoundExcepion;
import com.sorbSoft.CabAcademie.exception.WorkspaceNameIsAlreadyTaken;
import com.sorbSoft.CabAcademie.payload.SetupNewPasswordRequest;
import com.sorbSoft.CabAcademie.payload.ResetPasswordRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {
    @Autowired
    private UserServices userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${frontend.url}")
    private String FRONTEND_URL;

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

    private ResponseEntity<MessageResponse> saveUser(UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
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


    @PostMapping("/saveStudent")
    public  ResponseEntity<MessageResponse> saveStudent(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveOrganization")
    public  ResponseEntity<MessageResponse> saveOrganization(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }


    @PostMapping("/saveSchool")
    public  ResponseEntity<MessageResponse> saveSchool(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveTeacher")
    public  ResponseEntity<MessageResponse> saveTeacher(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/save")
    public  ResponseEntity<MessageResponse> saveOtherUser(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveByAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Add user by School/Org Admin, Role:ROLE_ADMIN")
    public  ResponseEntity<MessageResponse> saveByAdmin(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken {
        user.setIsDefaultPasswordChanged(false);
        return saveUser(user);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id ){
        Result result =  userService.deleteUser(id);
        if (!result.isValid()){
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        }
        return  new ResponseEntity<>(MessageResponse.of("Section successfully deleted"), HttpStatus.OK);
    }

    @GetMapping(value = "/confirm/email/{emailConfirmationUid}")
    public ResponseEntity<MessageResponse> confirmUserEmail(@PathVariable String emailConfirmationUid) {
        Result result = userService.confirmUserEmail(emailConfirmationUid);

        URI feLoginUrlSuccess = null;
        URI feLoginUrlError = null;
        try {
            feLoginUrlSuccess = new URI(FRONTEND_URL + "/login?msg=emailConfirmationSuccess");
            feLoginUrlError = new URI(FRONTEND_URL + "/login?msg=emailConfirmationError");
            log.debug("Frontend login url with Success message: " + feLoginUrlSuccess);
            log.debug("Frontend login url with Error message: " + feLoginUrlError);
        } catch (URISyntaxException e) {
            log.error("Can't construct frontend login url: " + e.getMessage());
            //e.printStackTrace();
        }

        if (!result.isValid()) {

            log.debug("Redirecting to: " + feLoginUrlError);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(feLoginUrlError);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }


        log.debug("Redirecting to: " + feLoginUrlSuccess);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(feLoginUrlSuccess);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping(value = "/getByWorkspaceName/{workspaceName}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserViewModel>> findUserByWorkspaceName(@PathVariable String workspaceName){
        List<UserViewModel> schoolOrOrganization = userService.findUserByWorkspaceName(workspaceName);
        if(schoolOrOrganization == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(schoolOrOrganization, HttpStatus.OK);
    }

    @PostMapping(value = "/reset-password" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetRq) throws UserNotFoundExcepion, EmptyValueException {

        if(resetRq.getEmail()==null || resetRq.getEmail().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        userService.sendResetPasswordEmail(resetRq.getEmail());

        return new ResponseEntity(MessageResponse.of("Reset password email sent"),HttpStatus.OK);
    }

    @PostMapping(value = "/setup-new-password" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> setupNewPassword(@Valid @RequestBody SetupNewPasswordRequest resetRq) throws UserNotFoundExcepion, PasswordsDoNotMatchException, EmptyValueException {

        userService.setupNewPassword(resetRq);

        return new ResponseEntity(MessageResponse.of("Password changed"),HttpStatus.OK);
    }
}
