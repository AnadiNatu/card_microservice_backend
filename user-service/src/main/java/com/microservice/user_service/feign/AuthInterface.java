package com.microservice.user_service.feign;

import com.microservice.user_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "auth-service" , url = "http://localhost:8085/api/auth")
public interface AuthInterface {

    @GetMapping("/api/auth/getLoggedInUser")
    public UserDTO getTheLoggedInUser();

    @GetMapping("/api/auth/getLoggedInUserName")
    public String getTheLoggedInUserName();

}
