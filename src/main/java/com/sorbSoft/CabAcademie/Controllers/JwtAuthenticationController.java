package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.JwtRequest;
import com.sorbSoft.CabAcademie.Entities.JwtResponse;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.SocialData;
import com.sorbSoft.CabAcademie.Services.UserServices;
import com.sorbSoft.CabAcademie.Services.email.EmailApiService;
import com.sorbSoft.CabAcademie.config.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import com.sorbSoft.CabAcademie.Services.JwtUserDetailsService;



@RestController
@Log4j2
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

       String authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
       if (!authentication.equals("")){
           return new ResponseEntity<>(MessageResponse.of(authentication), HttpStatus.BAD_REQUEST);
       }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

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
        /*final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);*/

        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/facebookLogin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Register user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")})
    public ResponseEntity<?> facebookLogin(SocialData data) {
        String token = null;

        Facebook facebook = new FacebookTemplate(data.getAccessToken());

        String[] fields = {"id", "email", "first_name", "last_name"};
        User userProfile = facebook.fetchObject("me", User.class, fields);

        if (userProfile != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userProfile.getEmail());

            token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return new ResponseEntity<>("Failed to authenticate", HttpStatus.UNAUTHORIZED);
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
