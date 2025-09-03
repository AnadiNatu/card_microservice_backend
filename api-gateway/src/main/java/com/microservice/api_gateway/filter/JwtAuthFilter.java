package com.microservice.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

@Component
public class JwtAuthFilter implements GlobalFilter , Ordered {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//
//        if (path.startsWith("/api/auth")){
//            return chain.filter(exchange);
//        }
//
//        String authHeader = request.getHeaders().getFirst("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")){
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            Claims claims = Jwts.parser()
//                    .verifyWith(getKey())
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//
//            ServerHttpRequest mutatedRequest = request
//                    .mutate()
//                    .header("X-User" , claims.getSubject())
//                    .header("X-Role" , claims.get("role" , String.class))
//                    .build();
//
//            return chain.filter(exchange.mutate().request(mutatedRequest).build());
//        }catch (JwtException ex){
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange , GatewayFilterChain chain){

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (
                path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/signup") ||
                path.startsWith("/api/auth/forgot-password") ||
                path.startsWith("/api/auth/reset-password")){
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try{
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            ServerHttpRequest mutatedRequest = request
                    .mutate()
                    .header("X-User-Email" , claims.getSubject())
                    .header("X-User-Role" , claims.get("role" , String.class))
                    .header("Authorization" , authHeader)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }catch (JwtException ex){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder(){
        return -1;
    }

}

