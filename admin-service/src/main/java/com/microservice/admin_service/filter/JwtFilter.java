package com.microservice.admin_service.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        String jwt = null;
//        String username = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")){
//            jwt = authHeader.substring(7);
//
//            try{
//                Claims claims = Jwts
//                        .parser()
//                        .setSigningKey(getSigningKey())
//                        .build()
//                        .parseClaimsJws(jwt)
//                        .getBody();
//
//                username = claims.getSubject();
//
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username , null , Collections.emptyList());
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }catch (ExpiredJwtException ex){
//                logger.warn("JWT token is expired : {}");
//            }catch (JwtException ex){
//                logger.warn("JWT token is invalid : {}");
//            }
//        }
//        filterChain.doFilter(request , response);
//    }
//
//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);  // this line of code converts the string into a string array for .hmacShaKeyFor() method
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException , IOException{

        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        String authHeader = request.getHeader("Authorization");

        if (userEmail != null && userRole != null && authHeader != null){
            try {
                String token = authHeader.substring(7);

                Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+userRole));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail , token , authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.debug("Set Authentication for user : {} with role {}");
            }catch (Exception e){
                logger.error("Error setting authentication");
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request , response);
    }
}
