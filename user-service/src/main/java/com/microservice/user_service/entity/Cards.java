package com.microservice.user_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.user_service.enums.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private String bankName;

    private String cvv;

    private LocalDate issuedAt;

    private LocalDate expireAt;

    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference //Prevents infinite recursion
    private Users users;


    @Override
    public String toString() {
        return "Cards{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cvv='" + cvv + '\'' +
                ", issuedAt=" + issuedAt +
                ", expireAt=" + expireAt +
                ", cardType=" + cardType +
                ", user=" + users +
                '}';
    }
}
