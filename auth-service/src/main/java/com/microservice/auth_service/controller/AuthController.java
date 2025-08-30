package com.microservice.auth_service.controller;


import com.microservice.auth_service.dto.*;
import com.microservice.auth_service.entity.Users;
import com.microservice.auth_service.enums.UserRoles;
import com.microservice.auth_service.repository.UserRepository;
import com.microservice.auth_service.security.JwtUtil;
import com.microservice.auth_service.security.UserServiceImpl;
import com.microservice.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signUpRequest){

        if (authService.hasUserWithEmail(signUpRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists");
        }

        UserDTO userDto = authService.signupUser(signUpRequest);

        if (userDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);

    }

//    @PostMapping("/signup/banker")
//    public ResponseEntity<?> signupBanker(@RequestBody BankerSignUpDTO signUpRequest){
//
//        if (authService.hasUserWithEmail(signUpRequest.getEmail())){
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists");
//        }
//
//        UserDTO userDto = authService.bankerSignup(signUpRequest);
//
//        if (userDto == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
//    }
//
//    @PostMapping("/signup/user")
//    public ResponseEntity<?> signupUser(@RequestBody UserSignUpDTO signUpRequest){
//
//        if (authService.hasUserWithEmail(signUpRequest.getEmail())){
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists");
//        }
//
//        UserDTO userDto = authService.userSignup(signUpRequest);
//
//        if (userDto == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
//    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            ));
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("Incorrect username or password");
        }

        UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<Users> optionalUsers = userRepository.findFirstByEmail(authenticationRequest.getUsername());
        String jwtToken = jwtUtil.generateToken(optionalUsers.get().getEmail());

        LoginResponse authResponse = new LoginResponse();

        if (optionalUsers.isPresent()){
            authResponse.setUserId(optionalUsers.get().getId());
            authResponse.setJwt(jwtToken);
            authResponse.setUserRoles(optionalUsers.get().getUserRoles());
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(authResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        authService.sendResetToken(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Reset token has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        authService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }

//  Feign Interface Methods
    @GetMapping("/getLoggedInUser")
    public UserDTO getTheLoggedInUser(){
        return (authService.getLoggedInUser());
    }

    @GetMapping("/getLoggedInUserName")
    public String getTheLoggedInUserName(){
        return authService.getLoggedInUsername();
    }

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers(){
        return authService.getAllUsers();
    }

}
