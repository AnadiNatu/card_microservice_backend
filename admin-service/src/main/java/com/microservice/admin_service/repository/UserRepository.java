package com.microservice.admin_service.repository;

import com.microservice.admin_service.entity.Users;
import com.microservice.admin_service.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Users> findFirstByEmail(@Param("email") String email);

    @Query("SELECT u FROM Users u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<Users> findByName(@Param("name") String name);

    @Query("SELECT u FROM Users u WHERE u.userRoles = :userRoles")
    List<Users> findAllUserByUserRole(@Param("userRoles") UserRoles userRoles);

    @Query("SELECT u FROM Users u JOIN u.cards c WHERE LOWER(c.bankName) = LOWER(:bankName) AND u.userRoles = :userRoles")
    List<Users> findAllUsersByBankNameAndUserRole(@Param("bankName") String bankName, @Param("userRoles") UserRoles userRoles);

}
