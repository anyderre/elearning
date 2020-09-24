package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Repository.UserRepository;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.UserInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.exception.*;
import com.sorbSoft.CabAcademie.payload.ChangeBatchPasswordRequest;
import com.sorbSoft.CabAcademie.payload.ChangePasswordRequest;
import com.sorbSoft.CabAcademie.payload.ResetPasswordRequest;
import com.sorbSoft.CabAcademie.payload.SetupNewPasswordRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
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

    @GetMapping(value = "/getById/{userId}")
    public ResponseEntity<UserViewModel> getUserById(@PathVariable Long userId){
        if(userId<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        UserViewModel vm= userService.findUserById(userId);
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

    @GetMapping(value = "/schoolProfessors/{page}/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STUDENT')")
    @ApiOperation(value = "Get School Teachers, Role:ROLE_ADMIN, ROLE_STUDENT")
    public ResponseEntity<Page<User>> findSchoolProfessors(
            @PathVariable Integer page,
            @PathVariable Integer amount,
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        Page<User> users = new PageImpl<>(new ArrayList<>());

        if(principal == null) {
            return new ResponseEntity(MessageResponse.of("You should be logged in as school member"), HttpStatus.FORBIDDEN);
        } else {
            users = userService.findSchoolProfessors(page, amount, principal.getName());
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/schoolProfessors/{schoolId}/{page}/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_PROFESSOR') or hasAuthority('ROLE_INSTRUCTOR') or hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get School/Org Teachers, Role:ROLE_PROFESSOR, ROLE_INSTRUCTOR, ROLE_SUPER_ADMIN")
    public ResponseEntity<Page<User>> findSchoolProfessorsForProfessor(
            @PathVariable Long schoolId,
            @PathVariable Integer page,
            @PathVariable Integer amount,
            Principal principal)
            throws EmptyValueException, UserNotFoundExcepion, SchoolNotFoundExcepion {

        Page<User> users = new PageImpl<>(new ArrayList<>());

        if(principal == null) {
            return new ResponseEntity(MessageResponse.of("You should be logged in as school member"), HttpStatus.FORBIDDEN);
        } else {
            users = userService.findSchoolProfessorsForProfessors(schoolId, page, amount, principal.getName());
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/publicProfessors/{page}/{amount}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> findPublicProfessors(
            @PathVariable Integer page,
            @PathVariable Integer amount){

        Page<User> users = userService.findPublicProfessors(page, amount);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }



    @GetMapping(value = "/professor/{id}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserViewModel> findProfessor(@PathVariable Long id) throws IOException {
        UserViewModel professor = userService.findProfessor(id);
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

    private ResponseEntity<MessageResponse> saveUser(UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        log.info("Save request");
        log.info(user.getUsername());
        log.info(user.getId());
        if (userRepository.existsByUsernameAndIdIsNot(user.getUsername(), user.getId()) ||
            userRepository.existsByUsername(user.getUsername()) > 0
                ) {
            log.info("1");

            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.of("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmailAndIdIsNot(user.getEmail(), user.getId()) ||
            userRepository.existsByEmail(user.getEmail()) > 0) {
            log.info("2");
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.of("Error: Email is already in use!"));
        }

        log.info("3");
        Result result = userService.saveUser(user);
        log.info("4");
        if(!result.isValid())
            return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of("User successfully created"), HttpStatus.CREATED);
    }


    @PostMapping("/saveStudent")
    public  ResponseEntity<MessageResponse> saveStudent(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveOrganization")
    public  ResponseEntity<MessageResponse> saveOrganization(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }


    @PostMapping("/saveSchool")
    public  ResponseEntity<MessageResponse> saveSchool(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveTeacher")
    public  ResponseEntity<MessageResponse> saveTeacher(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/save")
    public  ResponseEntity<MessageResponse> saveOtherUser(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
        user.setIsDefaultPasswordChanged(true);
        return saveUser(user);
    }

    @PostMapping("/saveByAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Add user by School/Org Admin, Role:ROLE_ADMIN")
    public  ResponseEntity<MessageResponse> saveByAdmin(@Valid @RequestBody UserViewModel user) throws WorkspaceNameIsAlreadyTaken, SubscriptionPlanNotSpecified {
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

    @PostMapping(value = "/change-password" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest resetRq,
            Principal principal) throws UserNotFoundExcepion, PasswordsDoNotMatchException, EmptyValueException {

        userService.changePassword(resetRq, principal.getName());

        return new ResponseEntity(MessageResponse.of("Password changed"),HttpStatus.OK);
    }

    @PostMapping(value = "/change-batch-password" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> changeBatchPassword(
            @Valid @RequestBody ChangeBatchPasswordRequest resetRq,
            Principal principal) throws UserNotFoundExcepion, PasswordsDoNotMatchException, EmptyValueException, UserIsNotBatchException {

        userService.changeBatchPassword(resetRq, principal.getName());

        return new ResponseEntity(MessageResponse.of("Password changed"),HttpStatus.OK);
    }
}
