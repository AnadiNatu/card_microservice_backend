package com.microservice.admin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateCardDTO {
    private String userName;
    private String bankName;
    private String cardType;

}
