package com.microservice.auth_service.dto;

import lombok.Data;

@Data
public class SignUpRequest {


    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String age;
    private String address;
    private String proof;
    private int roleNumber;
}
