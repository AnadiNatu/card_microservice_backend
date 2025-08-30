package com.microservice.user_service.controller;

import com.microservice.user_service.dto.*;
import com.microservice.user_service.entity.Users;
import com.microservice.user_service.exceptions.CardNotFoundException;
import com.microservice.user_service.exceptions.UserNotFoundException;
import com.microservice.user_service.feign.AuthInterface;
import com.microservice.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthInterface authInterface;

    @GetMapping("by-bank-and-user")
    public ResponseEntity<?> findCardByBankAndUser(@RequestBody CardFetchDTO dto){
        try{
            UserDTO users = authInterface.getTheLoggedInUser();
            CardInfoDTO card = userService.findCardBankNameAndUserName(dto.getBankName() , users.getName() , dto.getCardType(), dto.getCvv());
            return ResponseEntity.ok(card);
        }catch (CardNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Invalid Card Type");
        }
    }

    @GetMapping("filter-specific")
    public ResponseEntity<?> findCardByFilter(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date issuedAfter ,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expiredBefore,
            @RequestParam String name,
            @RequestParam String bankName,
            @RequestParam String cardType){

        try {
            CardInfoDTO card = userService.findCardByFilter(issuedAfter, expiredBefore, name, bankName, cardType);
            return ResponseEntity.ok(card);
        }catch (UserNotFoundException | CardNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Invalid card type");
        }
    }

    @GetMapping("user-role")
    public ResponseEntity<?> findUserByNameAndRole(@RequestParam String name , @RequestParam String role){
        try {
            UserInfoDTO userInfoDTO = userService.findUserByNameAndRole(name, role);
            return ResponseEntity.ok(userInfoDTO);
        }catch (UserNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Invalid User Roles .");
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateCardDetails(@RequestBody UpdateCardDetailDTO updateCardDetailDTO){
        try{
            CardInfoDTO updateCard = userService.updateCardDetails(updateCardDetailDTO);
            return ResponseEntity.ok(updateCard);
        }catch (CardNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Invalid Card Type");
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteCardByCardNumber(@RequestParam String cardNumber) {
        try {
            boolean deleted = userService.deleteCardByCardNumber(cardNumber);
            return ResponseEntity.ok("Card deleted successfully: " + deleted);
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
