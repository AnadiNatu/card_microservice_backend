package com.microservice.user_service.dto;

import com.microservice.user_service.entity.Cards;
import com.microservice.user_service.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBankerInfoDTO {

    private String name;
    private String phoneNumber;
    private String age;
    private String email;
    private String address;
    private String proof;
    private UserRoles userRoles = UserRoles.BANKER;
    private List<Cards> cards = null;

}
