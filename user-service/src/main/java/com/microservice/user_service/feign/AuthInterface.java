package com.microservice.user_service.feign;

import com.microservice.user_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// Use service discovery and a consistent base path; avoid hardcoded URLs
@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthInterface {

    @GetMapping("/getLoggedInUser")
    UserDTO getTheLoggedInUser();

    @GetMapping("/getLoggedInUserName")
    String getTheLoggedInUserName();
}
