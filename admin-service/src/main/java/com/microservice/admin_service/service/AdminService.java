package com.microservice.admin_service.service;

import com.microservice.admin_service.dto.*;
import com.microservice.admin_service.entity.Cards;
import com.microservice.admin_service.entity.Users;
import com.microservice.admin_service.enums.CardType;
import com.microservice.admin_service.enums.UserRoles;
import com.microservice.admin_service.exception.ResourceNotFoundException;
import com.microservice.admin_service.exception.UserNotFoundException;
import com.microservice.admin_service.feign.AuthInterface;
import com.microservice.admin_service.mapper.Mapper;
import com.microservice.admin_service.repository.CardRepository;
import com.microservice.admin_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private Mapper mapper;

    @Autowired
    private AuthInterface authInterface;

    public CardInfoDTO createCard(CreateCardDTO createCardDTO) throws IllegalAccessException {

        Users user = userRepository.findByName(createCardDTO.getUserName()).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Cards cards = mapper.mapToCreateCard(createCardDTO);
        cards.setUsers(user);

        Cards savedCard = cardRepository.save(cards);
        return mapper.mapFromCardToCardDTO(cards);
    }

    public List<CardInfoDTO> findAllCardsByCardType(String cardType){

        CardType type = CardType.valueOf(cardType.toUpperCase());

        return cardRepository
                .findAllByCardType(type)
                .stream()
                .map(mapper::mapFromCardToCardDTO)
                .collect(Collectors.toList());
    }

    public List<CardInfoDTO> findAllCardsByBankNameAndCardType(String bankName , String cardType){

        CardType type = CardType.valueOf(cardType.toUpperCase());

        return cardRepository
                .findAllCardsByBankNameAndCardType(bankName, type)
                .stream()
                .map(mapper::mapFromCardToCardDTO)
                .collect(Collectors.toList());

    }

    public List<CardInfoDTO> findAllCardByFilters(LocalDate issuedAfter , LocalDate expiredBefore , String bankName , String cardType){

        CardType type = CardType.valueOf(cardType.toUpperCase());
        return cardRepository
                .findAllCardByFilters(issuedAfter , expiredBefore , bankName , type)
                .stream()
                .map(mapper::mapFromCardToCardDTO)
                .collect(Collectors.toList());
    }

    public List<UserInfoDTO> findAllUserByRole(String userRoles){

        UserRoles role = UserRoles.valueOf(userRoles.toUpperCase());
        List<Users> users = userRepository.findAllUserByUserRole(role);

        return users
                .stream()
                .map(user -> {
                    List<CardInfoDTO> cards = cardRepository.findCardByUserId(user.getId())
                            .stream()
                            .map(mapper::mapFromCardToCardDTO)
                            .toList();
                    return mapper.mapFromUserToUserDTO(user , cards);
                }).toList();
    }

    public UserInfoDTO findUserByName(String name){

        List<Cards> cardsList = cardRepository.findCardByUsersName(name);

        Users users = userRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<CardInfoDTO> cardInfoDTO = new ArrayList<>();
        for (Cards cards : cardsList){
            cardInfoDTO.add(mapper.mapFromCardToCardDTO(cards));
        }
        return mapper.mapFromUserToUserDTO(users , cardInfoDTO);
    }

    public UserBankerInfoDTO findUserByBankerName(String bankerName){

        Users users = userRepository.findByName(bankerName).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapper.mapFromUserToBankerInfoDTO(users);
    }

    public List<?> findAllUsersByBankNameAndUserRole(String bankName , String userRoles){

        UserRoles role = UserRoles.valueOf(userRoles.toUpperCase());

        List<Users> userList = userRepository.findAllUsersByBankNameAndUserRole(bankName , role);

        List<UserInfoDTO> userInfoList = new ArrayList<>();
        List<UserBankerInfoDTO> bankerInfoList = new ArrayList<>();
        for (Users users : userList) {

            if (users.getUserRoles().equals(UserRoles.USER)) {
                List<Cards> cards = cardRepository.findAllCardByBankNameAndUser(bankName, users.getName(), role);
                List<CardInfoDTO> cardInfoList = new ArrayList<>();
                for (Cards card : cards) {
                    cardInfoList.add(mapper.mapFromCardToCardDTO(card));
                }
                userInfoList.add(mapper.mapFromUserToUserDTO(users , cardInfoList));
            }
            bankerInfoList.add(mapper.mapFromUserToBankerInfoDTO(users));
        }
        if (role == UserRoles.USER){
            return userInfoList;
        }
        return bankerInfoList;
    }

    public Map<String , Long> getCardCountPerBank(){
        List<Cards> allCards = cardRepository.findAll();
        return allCards
                .stream()
                .collect(Collectors
                        .groupingBy(
                                Cards::getBankName,Collectors.counting()
                        ));
    }

    public void saveAllUsers(){
        List<UserDTO> userDTOList = authInterface.getAllTheUsers();
        for (UserDTO dto : userDTOList){
            userRepository.save(mapper.mapFromUserDtoToUser(dto));
        }
    }
}
