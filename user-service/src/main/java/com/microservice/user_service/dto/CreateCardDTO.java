package com.microservice.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardDTO {
    private String userName;
    private String bankName;
    private String cardType;

}
