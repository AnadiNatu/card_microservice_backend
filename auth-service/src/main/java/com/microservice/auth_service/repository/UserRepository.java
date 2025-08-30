package com.microservice.auth_service.repository;


import com.microservice.auth_service.entity.Users;
import com.microservice.auth_service.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Users> findFirstByEmail(@Param("email") String username);

    @Query("SELECT u FROM Users u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<Users> findByName(@Param("name")String name);

    @Query("SELECT u FROM Users u WHERE u.userRoles = :userRoles")
    Optional<Users> findByUserRoles(@Param("userRoles") UserRoles userRoles);
}
