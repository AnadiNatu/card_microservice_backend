package com.microservice.user_service.dto;

import com.microservice.user_service.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCardInfoDTO {

    private Long id;
    private String cardNumber;
    private String bankName;
    private String cvv;
    private LocalDate issueAt;
    private LocalDate expireAt;
    private CardType cardType;

 }
