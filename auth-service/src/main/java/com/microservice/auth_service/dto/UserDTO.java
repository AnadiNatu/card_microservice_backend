package com.microservice.auth_service.dto;

import com.microservice.auth_service.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String name;
    private String phoneNumber;
    private String age;
    private String email;
    private String password;
    private String address;
    private String proof;
    private UserRoles userRoles;
}
