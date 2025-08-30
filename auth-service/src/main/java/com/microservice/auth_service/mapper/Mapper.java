package com.microservice.auth_service.mapper;

import com.microservice.auth_service.dto.BankerSignUpDTO;
import com.microservice.auth_service.dto.UserDTO;
import com.microservice.auth_service.dto.UserSignUpDTO;
import com.microservice.auth_service.entity.Users;
import com.microservice.auth_service.enums.UserRoles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public Users mapForUserFromBankerSignUpDTO(BankerSignUpDTO dto){
        Users users = new Users();
        users.setName(dto.getName());
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setAge(dto.getAge());
        users.setEmail(dto.getEmail());
        users.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        users.setAddress(dto.getAddress());
        users.setProof(dto.getProof());
        users.setUserRoles(UserRoles.BANKER);
        return users;
    }

    public Users mapForUserFromUsersSignUpDTO(UserSignUpDTO dto){
        Users users = new Users();
        users.setName(dto.getName());
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setAge(dto.getAge());
        users.setEmail(dto.getEmail());
        users.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        users.setAddress(dto.getAddress());
        users.setProof(dto.getProof());
        users.setUserRoles(UserRoles.USER);
        return users;
    }

    public UserDTO mapForUserDTOFromUser(Users dto){
        UserDTO users = new UserDTO();
        users.setName(dto.getName());
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setAge(dto.getAge());
        users.setEmail(dto.getEmail());
        users.setPassword(dto.getPassword());
        users.setAddress(dto.getAddress());
        users.setProof(dto.getProof());
        users.setUserRoles(dto.getUserRoles());
        return users;
    }

    public UserDTO mapForUserToJustUserDTO(Users bankerInfoDTO){
        UserDTO users = new UserDTO();

        users.setUserId(bankerInfoDTO.getId());
        users.setName(bankerInfoDTO.getName());
        users.setPhoneNumber(bankerInfoDTO.getPhoneNumber());
        users.setAge(bankerInfoDTO.getAge());
        users.setEmail(bankerInfoDTO.getEmail());
        users.setAddress(bankerInfoDTO.getAddress());
        users.setProof(bankerInfoDTO.getProof());
        users.setUserRoles(bankerInfoDTO.getUserRoles());

        return users;
    }
}
