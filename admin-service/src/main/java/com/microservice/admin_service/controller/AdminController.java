package com.microservice.admin_service.controller;

import com.microservice.admin_service.dto.*;
import com.microservice.admin_service.exception.ResourceNotFoundException;
import com.microservice.admin_service.exception.UserNotFoundException;
import com.microservice.admin_service.feign.AuthInterface;
import com.microservice.admin_service.repository.UserRepository;
import com.microservice.admin_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthInterface authInterface;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("create")
    public ResponseEntity<CardInfoDTO> createCard(@RequestBody CreateCardDTO cardDTO){
        try {
            CardInfoDTO response = adminService.createCard(cardDTO);
            return new ResponseEntity<>(response , HttpStatus.CREATED);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(null , HttpStatus.FORBIDDEN);
        }catch (UserNotFoundException ex){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }catch(Exception ex){
            return new ResponseEntity<>(null , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("type/{cardType}")
    public ResponseEntity<List<CardInfoDTO>> getCardsByType(@PathVariable String cardType){

        try
        {
            List<CardInfoDTO> result = adminService.findAllCardsByCardType(cardType);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("bank/{bankName}/type/{cardType}")
    public ResponseEntity<List<CardInfoDTO>> getCardByBankAndType(@PathVariable String bankName , @PathVariable String cardType){

        try{
            List<CardInfoDTO> result = adminService.findAllCardsByBankNameAndCardType(bankName, cardType);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("filter")
    public ResponseEntity<List<CardInfoDTO>> filterCard( @RequestParam("issuedAfter") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issuedAfter,
                                                         @RequestParam("expiredBefore") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiredBefore,
                                                         @RequestParam("bankName") String bankName,
                                                         @RequestParam("cardType") String cardType){

        try{
            List<CardInfoDTO> result = adminService.findAllCardByFilters(issuedAfter, expiredBefore, bankName, cardType);
            return ResponseEntity.ok(result);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("users/role/{userRole}")
    public ResponseEntity<List<UserInfoDTO>> getUserByRole(@PathVariable String userRoles){

            try {
                List<UserInfoDTO> result = adminService.findAllUserByRole(userRoles);
                return ResponseEntity.ok(result);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }

    }

    @GetMapping("user/name/{name}")
    public ResponseEntity<UserInfoDTO> getUserByName(@PathVariable String name){

        try{
            UserInfoDTO result = adminService.findUserByName(name);
            return ResponseEntity.ok(result);
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("banker/name/{bankerName}")
    public ResponseEntity<UserBankerInfoDTO> getBankerByName(@PathVariable String bankerName){
        try{
            UserBankerInfoDTO result = adminService.findUserByBankerName(bankerName);
            return ResponseEntity.ok(result);
        }catch (ResourceNotFoundException ex){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("user")
    public ResponseEntity<?> getUsersByBankAndRole(@RequestParam String bankName , @RequestParam String userRoles){
        try{
            return ResponseEntity.ok(adminService.findAllUsersByBankNameAndUserRole(bankName, userRoles));

        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Invalid User Role Provided");
        }
    }

    @GetMapping("count/bank")
    public ResponseEntity<Map<String , Long>> getCardCountBank(){
        return ResponseEntity.ok(adminService.getCardCountPerBank());
    }

//    Feign Interface from Auth-Service functions
    @GetMapping("getUser")
    public ResponseEntity<UserDTO> getLoggedInUser(){
        return ResponseEntity.ok(authInterface.getTheLoggedInUser());
    }

    @GetMapping("getUserName")
    public ResponseEntity<String> getLoggedInUserName(){
        return ResponseEntity.ok(authInterface.getTheLoggedInUserName());
    }

    @GetMapping("getAllUser")
    public void getAllUser(){
        adminService.saveAllUsers();
    }
//    Feign Interface from User-Service functions





//    Feign Interface from Banker-Service functions


}

