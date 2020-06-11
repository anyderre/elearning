package com.sorbSoft.CabAcademie.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sorbSoft.CabAcademie.Entities.Enums.Roles;
import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.JwtRequest;
import com.sorbSoft.CabAcademie.Entities.JwtResponse;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Services.Dtos.Validation.Result;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.UserViewModel;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.social.SocialRequest;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.social.SocialResponse;
import com.sorbSoft.CabAcademie.Services.JwtUserDetailsService;
import com.sorbSoft.CabAcademie.Services.LinkedinOAuth2Service;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.config.JwtTokenUtil;
import com.sorbSoft.CabAcademie.exception.WorkspaceNameIsAlreadyTaken;
import com.sorbSoft.CabAcademie.model.linkedin.LinkedinUserProfile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.oauth2.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@Log4j2
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserServices userService;

    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @Autowired
    private LinkedinOAuth2Service linkedin;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

       String authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
       if (!authentication.equals("")){
           return new ResponseEntity<>(MessageResponse.of(authentication), HttpStatus.BAD_REQUEST);
       }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

       if(!userDetails.isEnabled()) {
           return new ResponseEntity<>("Please confirm your email", HttpStatus.FORBIDDEN);
       }

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @RequestMapping(value = "/jitsi-auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateJistsi(@RequestBody JwtRequest authenticationRequest) throws Exception {

        log.info("************ Jitsi auth called *********");
        String authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (!authentication.equals("")){
            return new ResponseEntity<>(MessageResponse.of(authentication), HttpStatus.BAD_REQUEST);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        if(!userDetails.isEnabled()) {
            return new ResponseEntity<>("Please confirm your email", HttpStatus.FORBIDDEN);
        }

        //TODO: check if user has appointment
        //TODO: check if user logged in only from 1 pc

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/facebookLogin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "SignUp & Login facebook user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public ResponseEntity<?> facebookSignUp(@Valid @RequestBody SocialRequest socialRequest) throws WorkspaceNameIsAlreadyTaken {
        String token = null;
        log.debug("FB signup started");
        Facebook facebook = new FacebookTemplate(socialRequest.getAccessToken());

        String[] fields = {"id", "email", "first_name", "last_name"};
        //TODO: fetch photo
        User userProfile = facebook.fetchObject("me", User.class, fields);
        if (userProfile != null) {
            log.debug("Fetched fb data: ", userProfile);
            UserDetails userDetails = null;

            try{
                userDetails = userDetailsService.loadUserByUsername(userProfile.getEmail());
            } catch (UsernameNotFoundException e) {
                log.debug("User with email "+userProfile.getEmail()+" was not found in db");
            }

            if(userDetails == null) {

                Rol freeStudent = new Rol();
                freeStudent.setRole(Roles.ROLE_FREE_STUDENT);
                freeStudent.setId(6L);

                UserViewModel user = new UserViewModel();
                user.setFacebookId(userProfile.getId());
                user.setFirstName(userProfile.getFirstName());
                user.setLastName(userProfile.getLastName());
                user.setUsername(userProfile.getEmail());
                user.setEmail(userProfile.getEmail());
                user.setAgreeWithTerms(true);
                user.setRole(freeStudent);
                user.setSocialUser(true);
                user.setEnable(1);
                user.setTimeZone(socialRequest.getUserTimeZone());
                user.setPassword(userProfile.getEmail()+userProfile.getFirstName());
                //TODO: add photo

                Result result = userService.saveSocialUser(user);
                if(!result.isValid())
                    return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

                userDetails = userDetailsService.loadUserByUsername(userProfile.getEmail());
                token = jwtTokenUtil.generateToken(userDetails);

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userProfile.getEmail());

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Registration");

                log.debug("Facebook user has been registered: ", socialResponse);
                return ResponseEntity.ok(socialResponse);

            } else {
                log.debug("User with "+ userProfile.getEmail() +" email already exist in db");

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userProfile.getEmail());

                token = jwtTokenUtil.generateToken(userDetails);

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Login");


                log.debug("Facebook user has been logged in: ", socialResponse);
                //success
                return ResponseEntity.ok(socialResponse);
            }

        } else {
            log.error("Failed to fetch social data from facebook");
            return new ResponseEntity<>("Failed to fetch social data from facebook", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(value = "/googleLogin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "SignUp or Login google user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public ResponseEntity<?> googleAuth(@Valid @RequestBody SocialRequest socialRequest) throws WorkspaceNameIsAlreadyTaken {
        String token = null;
        log.debug("Google signup started");
        Google google = new GoogleTemplate(socialRequest.getAccessToken());
        UserInfo userinfo = google.oauth2Operations().getUserinfo();
        if (userinfo != null) {
            log.debug("Fetched google data: ", userinfo.toString());
            UserDetails userDetails = null;

            try{
                userDetails = userDetailsService.loadUserByUsername(userinfo.getEmail());
            } catch (UsernameNotFoundException e) {
                log.debug("User with email "+userinfo.getEmail()+" was not found in db");
            }

            if(userDetails == null) {

                Rol freeStudent = new Rol();
                freeStudent.setRole(Roles.ROLE_FREE_STUDENT);
                freeStudent.setId(6L);

                UserViewModel user = new UserViewModel();
                user.setGoogleId(userinfo.getId());
                user.setFirstName(userinfo.getGivenName());
                user.setLastName(userinfo.getFamilyName());
                user.setUsername(userinfo.getEmail());
                user.setEmail(userinfo.getEmail());
                user.setAgreeWithTerms(true);
                user.setRole(freeStudent);
                user.setSocialUser(true);
                user.setEnable(1);

                user.setPhotoURL(userinfo.getPicture());
                user.setTimeZone(socialRequest.getUserTimeZone());
                user.setPassword(userinfo.getEmail()+userinfo.getGivenName());


                Result result = userService.saveSocialUser(user);
                if(!result.isValid())
                    return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

                userDetails = userDetailsService.loadUserByUsername(userinfo.getEmail());

                token = jwtTokenUtil.generateToken(userDetails);

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userinfo.getEmail());

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Registration");

                log.debug("Google user has been registered: ", socialResponse);
                return ResponseEntity.ok(socialResponse);

            } else {
                log.debug("User with "+ userinfo.getEmail() +" email already exist in db");

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userinfo.getEmail());

                token = jwtTokenUtil.generateToken(userDetails);

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Login");

                log.debug("Google user has been logged In: ", socialResponse);
                //success
                return ResponseEntity.ok(socialResponse);
            }

        } else {
            log.error("Failed to fetch social data from google");
            return new ResponseEntity<>("Failed to fetch social data from google", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(value = "/linkedinLogin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Auth linkedin user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public ResponseEntity<?> linkedinAuth(@Valid @RequestBody SocialRequest socialRequest) throws JsonProcessingException, WorkspaceNameIsAlreadyTaken {
        String token = null;
        log.debug("Linkedin signup started");
        LinkedinUserProfile userInfo = linkedin.fetchUserProfile(socialRequest.getAccessToken());

        log.debug("Fetched linkedin data: ", userInfo.toString());
        if (userInfo != null) {
            UserDetails userDetails = null;

            try{
                userDetails = userDetailsService.loadUserByUsername(userInfo.getEmailAddress());
            } catch (UsernameNotFoundException e) {
                log.debug("User with email "+userInfo.getEmailAddress()+" was not found in db");
            }

            if(userDetails == null) { //signup

                Rol freeStudent = new Rol();
                freeStudent.setId(6L);
                freeStudent.setRole(Roles.ROLE_FREE_STUDENT);

                UserViewModel user = new UserViewModel();
                user.setGoogleId(userInfo.getId());
                user.setFirstName(userInfo.getFirstName());
                user.setLastName(userInfo.getLastName());
                user.setUsername(userInfo.getEmailAddress());
                user.setEmail(userInfo.getEmailAddress());
                user.setAgreeWithTerms(true);
                user.setRole(freeStudent);
                user.setSocialUser(true);
                user.setEnable(1);

                user.setPhotoURL(userInfo.getProfilePictureUrl());
                user.setTimeZone(socialRequest.getUserTimeZone());
                user.setPassword(userInfo.getEmailAddress()+userInfo.getFirstName());


                Result result = userService.saveSocialUser(user);
                if(!result.isValid())
                    return new ResponseEntity<>(MessageResponse.of(result.lista.get(0).getMessage()), HttpStatus.CONFLICT);

                userDetails = userDetailsService.loadUserByUsername(userInfo.getEmailAddress());

                token = jwtTokenUtil.generateToken(userDetails);

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userInfo.getEmailAddress());

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Registration");

                log.debug("Linkedin user has been registered: ", socialResponse);
                return ResponseEntity.ok(socialResponse);

            } else {
                log.debug("User with "+ userInfo.getEmailAddress() +" email already exist in db");

                com.sorbSoft.CabAcademie.Entities.User userbyUsername = userService.findUserbyUsername(userInfo.getEmailAddress());

                token = jwtTokenUtil.generateToken(userDetails);

                SocialResponse socialResponse = new SocialResponse();
                socialResponse.setUser(userbyUsername);
                socialResponse.setToken(token);
                socialResponse.setType("Login");

                log.debug("Linkedin user has been logged In: ", socialResponse);
                //success
                return ResponseEntity.ok(socialResponse);
            }

        } else {
            log.error("Failed to fetch social data from linkedin");
            return new ResponseEntity<>("Failed to fetch social data from linkedin", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private String authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
//            throw new Exception("USER_DISABLED", e);
            return "USER_DISABLED";
        } catch (BadCredentialsException e) {
            return "INVALID_CREDENTIALS";
//            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return "";
    }
}
