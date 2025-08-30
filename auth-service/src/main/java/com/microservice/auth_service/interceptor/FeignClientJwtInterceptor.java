package com.microservice.auth_service.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientJwtInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken token && token.getCredentials() instanceof String jwt) {
            template.header("Authorization", "Bearer " + jwt);
        } else {
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