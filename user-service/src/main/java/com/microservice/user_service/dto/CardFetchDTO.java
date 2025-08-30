package com.microservice.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CardFetchDTO {

    private String bankName;
    private String cardType;
    private String cvv;

}
