package com.microservice.admin_service.feign;

import com.microservice.admin_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "auth-service")
public interface AuthInterface {
    @GetMapping("/api/auth/getAllUsers")
    public List<UserDTO> getAllTheUsers();

    @GetMapping("/api/auth/getLoggedInUser")
    public UserDTO getTheLoggedInUser();

    @GetMapping("/api/auth/getLoggedInUserName")
    public String getTheLoggedInUserName();
}
