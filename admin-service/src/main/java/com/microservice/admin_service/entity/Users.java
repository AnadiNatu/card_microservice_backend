package com.microservice.admin_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservice.admin_service.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users" , uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone_number")
})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 100)
    private String name;

    @Column(name = "phone_number" , nullable = false , length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false , unique = true , length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String proof;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_roles" , nullable = false)
    private UserRoles userRoles;

    @Column(name = "reset_token")
    private String resetToken;

    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Cards> cards = new ArrayList<>();

    @Column(name = "created_at" , nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
