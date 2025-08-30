package com.microservice.user_service.mapper;

import com.microservice.user_service.dto.*;
import com.microservice.user_service.entity.Cards;
import com.microservice.user_service.entity.Users;
import com.microservice.user_service.enums.CardType;
import com.microservice.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class CardMapper {
    @Autowired
    private UserRepository userRepository;

    public CardInfoDTO mapFromCardToCardDTO(Cards cards) {
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setCardId(cards.getId());
        cardInfoDTO.setCardNumber(cards.getCardNumber());
        cardInfoDTO.setBankNumber(cards.getBankName());
        cardInfoDTO.setCardType(String.valueOf(cards.getCardType()));
        cardInfoDTO.setIssueAt(cards.getIssuedAt());
        cardInfoDTO.setExpireAt(cards.getExpireAt());
        return cardInfoDTO;
    }

    public UserInfoDTO mapFromUserToUserDTO(Users users, List<CardInfoDTO> userCardList) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName(users.getName());
        userInfoDTO.setPhoneNumber(users.getPhoneNumber());
        userInfoDTO.setAge(users.getAge());
        userInfoDTO.setEmail(users.getEmail());
        userInfoDTO.setAddress(users.getAddress());
        userInfoDTO.setProof(users.getProof());
        userInfoDTO.setUserRoles(String.valueOf(users.getUserRoles()));
        userInfoDTO.setUserCardList(userCardList);
        return userInfoDTO;
    }

    public Cards mapToCreateCard(CreateCardDTO cardDTO) throws IllegalAccessException {
        Cards cards = new Cards();

        String bankName = cardDTO.getBankName();
        cards.setBankName(bankName);

        String cardPrefix = mapBankNameToCardNumberPrefix(bankName);
        cards.setCardNumber(cardPrefix + generateRandomDigits(12));

        cards.setIssuedAt(convertDateToLocalDate(new Date()));
        cards.setExpireAt((calculateExpiryDate()));

        int cvvStart = mapBankNameToCvvRangeStart(bankName);
        int cvvEnd = mapBankNameToCvvRangeEnd(bankName);
        cards.setCvv(String.valueOf(generateRandomNumber(cvvStart, cvvEnd)));

        if (cardDTO.getCardType().equalsIgnoreCase("Credit")) {
            cards.setCardType(CardType.CREDIT);
        } else if (cardDTO.getCardType().equalsIgnoreCase("Debit")) {
            cards.setCardType(CardType.DEBIT);
        } else {
            cards.setCardType(CardType.VISA);
        }
        return cards;
    }

    private String mapBankNameToCardNumberPrefix(String bankName) throws IllegalArgumentException {
        return switch (bankName.toLowerCase()) {
            case "hdfc" -> "1234";
            case "citi" -> "5678";
            case "icici" -> "6789";
            default -> throw new IllegalArgumentException("Invalid Bank Nme");
        };
    }


    private int generateRandomNumber(int cvvStart, int cvvEnd) {
        Random random = new Random();
        return random.nextInt(cvvEnd - cvvStart + 1) + cvvStart;
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private int mapBankNameToCvvRangeStart(String bankName) throws IllegalAccessException {
        return switch (bankName.toLowerCase()) {
            case "hdfc" -> 200;
            case "citi" -> 500;
            case "icici" -> 600;
            default -> throw new IllegalAccessException("Invalid Bank Name");
        };
    }

    public int mapBankNameToCvvRangeEnd(String bankName) throws IllegalAccessException {
        return switch (bankName.toLowerCase()) {
            case "hdfc" -> 500;
            case "citi" -> 700;
            case "icici" -> 800;
            default -> throw new IllegalAccessException("Invalid Bank Name");
        };
    }

    public LocalDate calculateExpiryDate() {
        Date curDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.YEAR, 2);
        return calendar
                .getTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDate convertDateToLocalDate(Date date){
        if (date == null){
            return null;
        }
        return date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public AllCardInfoDTO mapFromCardsToAllCardInfoDTO(Cards cards){
        AllCardInfoDTO cardDTO = new AllCardInfoDTO();

        cardDTO.setId(cards.getId());
        cardDTO.setCardNumber(cards.getCardNumber());
        cardDTO.setBankName(cards.getBankName());
        cardDTO.setCvv(cards.getCvv());
        cardDTO.setIssueAt(cards.getIssuedAt());
        cardDTO.setExpireAt(cards.getExpireAt());
        cardDTO.setCardType(cards.getCardType());

        return cardDTO;
    }

    public UserInfoDTO mapFromUserToUserInfoDTO(Users users){
        UserInfoDTO bankerInfoDTO = new UserInfoDTO();

        bankerInfoDTO.setName(users.getName());
        bankerInfoDTO.setPhoneNumber(users.getPhoneNumber());
        bankerInfoDTO.setAge(users.getAge());
        bankerInfoDTO.setEmail(users.getEmail());
        bankerInfoDTO.setAddress(users.getAddress());
        bankerInfoDTO.setProof(users.getProof());

        return bankerInfoDTO;
    }

    public UserBankerInfoDTO mapFromUserToBankerInfoDTO(Users users){
        UserBankerInfoDTO bankerInfoDTO = new UserBankerInfoDTO();

        bankerInfoDTO.setName(users.getName());
        bankerInfoDTO.setPhoneNumber(users.getPhoneNumber());
        bankerInfoDTO.setAge(users.getAge());
        bankerInfoDTO.setEmail(users.getEmail());
        bankerInfoDTO.setAddress(users.getAddress());
        bankerInfoDTO.setProof(users.getProof());

        return bankerInfoDTO;
    }

//    public UserDTO mapForUserToJustUserDTO(Users bankerInfoDTO){
//        UserDTO users = new UserDTO();
//
//        users.setName(bankerInfoDTO.getName());
//        users.setPhoneNumber(bankerInfoDTO.getPhoneNumber());
//        users.setAge(bankerInfoDTO.getAge());
//        users.setEmail(bankerInfoDTO.getEmail());
//        users.setAddress(bankerInfoDTO.getAddress());
//        users.setProof(bankerInfoDTO.getProof());
//        users.setUserRoles(bankerInfoDTO.getUserRoles());
//
//        return users;
//    }

    public List<CardExcelDTO> toCardExcelDto(List<Cards> cardsList) {
        List<CardExcelDTO> excelDTOList = new ArrayList<>();
        for (Cards card : cardsList){
            CardExcelDTO excelDTO = new CardExcelDTO();
            excelDTO.setName(card.getUsers() != null ? card.getUsers().getName() : null);
            excelDTO.setEmail(card.getUsers() != null ? card.getUsers().getEmail() : null);
            excelDTO.setUserRoles(card.getUsers() != null ? card.getUsers().getUserRoles() : null);
            excelDTO.setCardNumber(card.getCardNumber());
            excelDTO.setBankName(card.getBankName());
            excelDTO.setIssuedAt(card.getIssuedAt());
            excelDTO.setExpiredAt(card.getExpireAt());
            excelDTO.setCardType(card.getCardType());
            excelDTOList.add(excelDTO);
        }
        return excelDTOList;
    }
}
