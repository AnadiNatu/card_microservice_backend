package com.microservice.user_service.repository;

import com.microservice.user_service.entity.Cards;
import com.microservice.user_service.enums.CardType;
import com.microservice.user_service.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Cards, Long> {

    List<Cards> findAllByCardType(CardType cardType);

    @Query("SELECT c FROM Cards c WHERE (c.cardNumber) = (:cardNumber)")
    Optional<Cards> findCardByCardNumber(@Param("cardNumber") String cardNumber);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE LOWER(c.bankName) = LOWER(:bankName) AND LOWER(u.name) = LOWER(:name) AND u.userRoles = :userRoles")
    List<Cards> findAllCardByBankNameAndUser(@Param("bankName") String bankName, @Param("name") String name, @Param("userRoles") UserRoles userRoles);

    @Query("SELECT c FROM Cards c WHERE LOWER(c.bankName) = LOWER(:bankName) AND c.cardType = :cardType")
    List<Cards> findAllCardsByBankNameAndCardType(@Param("bankName") String bankName, @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE LOWER(c.bankName) = LOWER(:bankName) AND LOWER(u.name) = LOWER(:name) AND c.cardType = :cardType")
    List<Cards> findAllCardByBankNameUserNameAndCardType(@Param("bankName") String bankName, @Param("name") String name, @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c WHERE c.cardType = :cardType AND LOWER(c.bankName) = LOWER(:bankName)")
    List<Cards> findAllCardsByBankNameAndCardTypes(@Param("bankName") String bankName, @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE LOWER(u.name) = LOWER(:name) AND LOWER(c.bankName) = LOWER(:bankName) AND c.cardType = :cardType")
    Optional<Cards> findCardByUsernameBankNameAndCardType(@Param("name") String name, @Param("bankName") String bankName, @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE LOWER(u.name) = LOWER(:name) AND LOWER(c.bankName) = LOWER(:bankName) AND c.cardType = :cardType AND c.cvv = :cvv")
    Optional<Cards> findSpecificCard(@Param("name")String name, @Param("bankName")String bankName , @Param("cardType")CardType cardType , @Param("cvv")String cvv);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE " +
            "(:issueAt IS NULL OR c.issuedAt >= :issueAt) AND " +
            "(:expireAt IS NULL OR c.expireAt <= :expireAt) AND " +
            "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:bankName IS NULL OR LOWER(c.bankName) LIKE LOWER(CONCAT('%', :bankName, '%'))) AND " +
            "(:cardType IS NULL OR c.cardType = :cardType)")
    Optional<Cards> findSpecificCardByFilters(@Param("issueAt") LocalDate issueAt,
                                              @Param("expireAt") LocalDate expireAt,
                                              @Param("name") String name,
                                              @Param("bankName") String bankName,
                                              @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE " +
            "(:issuedAt IS NULL OR c.issuedAt >= :issueAt) AND " +
            "(:expireAt IS NULL OR c.expireAt <= :expireAt) AND " +
            "(:bankName IS NULL OR LOWER(c.bankName) LIKE LOWER(CONCAT('%', :bankName, '%'))) AND " +
            "(:cardType IS NULL OR c.cardType = :cardType)")
    List<Cards> findAllCardByFilters(@Param("issueAt") LocalDate issuedAt,
                                     @Param("expireAt") LocalDate expiredAt,
                                     @Param("bankName") String bankName,
                                     @Param("cardType") CardType cardType);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE u.id = :user_id")
    List<Cards> findCardByUserId(@Param("user_id") Long user_id);

    @Query("SELECT c FROM Cards c JOIN c.users u WHERE u.name = :name")
    List<Cards> findCardByUsersName(@Param("name") String name);


}
