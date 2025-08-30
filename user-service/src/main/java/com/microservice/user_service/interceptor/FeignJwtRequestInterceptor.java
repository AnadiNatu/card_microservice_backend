package com.microservice.user_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignJwtRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String token) {
            template.header("Authorization", "Bearer " + token);
        }else {
            String jwt = extractTokenFromSecurityContext();
            if (jwt != null) template.header("Authorization", "Bearer " + jwt);
        }
    }

    private String extractTokenFromSecurityContext() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().getCredentials() instanceof String token) {
            return token;
        }
        return null;
    }
}
