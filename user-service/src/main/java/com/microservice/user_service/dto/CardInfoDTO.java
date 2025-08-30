package com.microservice.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardInfoDTO {
    private Long cardId;
    private String cardNumber;
    private String bankNumber;
    private LocalDate issueAt;
    private LocalDate expireAt;
    private String cardType;
}
