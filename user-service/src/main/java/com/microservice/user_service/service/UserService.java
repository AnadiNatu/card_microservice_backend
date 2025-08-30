package com.microservice.user_service.service;

import com.microservice.user_service.dto.CardInfoDTO;
import com.microservice.user_service.dto.UpdateCardDetailDTO;
import com.microservice.user_service.dto.UserInfoDTO;
import com.microservice.user_service.entity.Cards;
import com.microservice.user_service.entity.Users;
import com.microservice.user_service.enums.CardType;
import com.microservice.user_service.enums.UserRoles;
import com.microservice.user_service.exceptions.CardNotFoundException;
import com.microservice.user_service.exceptions.UserNotFoundException;
import com.microservice.user_service.mapper.CardMapper;
import com.microservice.user_service.repository.CardRepository;
import com.microservice.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.microservice.user_service.mapper.CardMapper.convertDateToLocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper mapper;

    public CardInfoDTO findCardBankNameAndUserName(String bankName , String name , String cardType , String cvv){
        CardType type = CardType.valueOf(cardType.toUpperCase());
        Cards cards = cardRepository.findSpecificCard(bankName , name , type , cvv).orElseThrow(() -> new CardNotFoundException("Card not Found"));
        return mapper.mapFromCardToCardDTO(cards);
    }

    public CardInfoDTO findCardByFilter(Date issuedAfter ,Date expiredBefore ,String name ,String bankName , String cardType){
        Users users = userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("User not Found"));
        CardType type = CardType.valueOf(cardType.toLowerCase());
        Cards cards = cardRepository.findSpecificCardByFilters(convertDateToLocalDate(issuedAfter) , convertDateToLocalDate(expiredBefore) , name , bankName , type).orElseThrow(() -> new CardNotFoundException("Card Not Found"));
        return mapper.mapFromCardToCardDTO(cards);
    }

    public UserInfoDTO findUserByNameAndRole(String name , String role){
        UserRoles roles = UserRoles.valueOf(role.toUpperCase());
        Users user = userRepository.findByName(name).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<Cards> cardsList = cardRepository.findCardByUserId(user.getId());
        List<CardInfoDTO> cardInfoList = new ArrayList<>();
        for (Cards cards : cardsList){
            cardInfoList.add(mapper.mapFromCardToCardDTO(cards));
        }
        return mapper.mapFromUserToUserDTO(user , cardInfoList);
    }

    public CardInfoDTO updateCardDetails(UpdateCardDetailDTO cardDetailDTO){
        Cards cards = cardRepository.findCardByCardNumber(cardDetailDTO.getCardNumber()).orElseThrow(() -> new CardNotFoundException("Card Not Found"));
        cards.setBankName(cardDetailDTO.getBankName());
        cards.setIssuedAt(convertDateToLocalDate(cardDetailDTO.getIssueAt()));
        cards.setExpireAt(convertDateToLocalDate(cardDetailDTO.getExpireAt()));
        cards.setCardType(CardType.valueOf(cardDetailDTO.getCardType().toLowerCase()));
        return mapper.mapFromCardToCardDTO(cards);
    }

    public boolean deleteCardByCardNumber(String cardNumber){
        Cards cards = cardRepository.findCardByCardNumber(cardNumber).orElseThrow(() -> new CardNotFoundException("Card Not Found"));
        cardRepository.deleteById(cards.getId());
        return true;
    }

}
