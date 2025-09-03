package com.microservice.api_gateway.configuration;

import com.microservice.api_gateway.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){

        return builder.routes()
                .route("auth-service" , r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route("admin-service" , r -> r.path("/api/admin/**")
                        .filters(f -> f.filter((GatewayFilter) jwtAuthFilter))
                        .uri("lb://admin-service"))
                .route("banker-service" , r -> r.path("/api/banker/**")
                        .filters(f -> f.filter((GatewayFilter) jwtAuthFilter))
                        .uri("lb://banker-service"))
                .route("user-service" , r -> r.path("/api/banker/**")
                        .filters(f -> f.filter((GatewayFilter) jwtAuthFilter))
                        .uri("lb://user-service"))
                .route("admin-service" , r -> r.path("/api/admin/**")
                        .filters(f -> f.filter((GatewayFilter) jwtAuthFilter))
                        .uri("lb://admin-service"))
                .build();

    }
}
