package com.microservice.admin_service.dto;

import com.microservice.admin_service.enums.CardType;
import com.microservice.admin_service.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardExcelDTO {

    private String name;
    private String email;
    private UserRoles userRoles;
    private String cardNumber;
    private String bankName;
    private LocalDate issuedAt;
    private LocalDate expiredAt;
    private CardType cardType;

}
