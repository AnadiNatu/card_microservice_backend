package com.microservice.admin_service.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.admin_service.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "cards")
public class Cards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number" , nullable = false , unique = true , length = 16)
    private String cardNumber;

    @Column(name = "bank_name" , nullable = false , length = 1000)
    private String bankName;

    @Column(name = "cvv" , nullable = false , length = 3)
    private String cvv;

    @Column(name = "issued_at" , nullable = false)
    private LocalDate issueAt;

    @Column(name = "expired_at" , nullable = false)
    private LocalDate expireAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type" , nullable = false)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    @JsonBackReference
    private Users users;

    @Column(name = "created_at" , nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , nullable = false )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }

}
