package com.microservice.auth_service.security;

import com.microservice.auth_service.entity.Users;
import com.microservice.auth_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();

        Users user = userRepository.findFirstByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User (
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getUserRoles().name())));

        claims.put("role" , user.getUserRoles().name());
        claims.put("authorities" , userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000*30*60))
                .and()
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);  // this line of code converts the string into a string array for .hmacShaKeyFor() method
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token){
        return extractClaim(token , Claims::getExpiration);
    }

    public List<GrantedAuthority> extractAuthorities(String token){

        Claims claim = extractAllClaims(token);

        List<String> roles = claim.get("authorities" , List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Users getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()){
            String email = authentication.getName();
            Optional<Users> optionalUser = userRepository.findByName(email);
//            Users user = (Users) authentication.getPrincipal();
//            Optional<Users> optionalUser= userRepository.findById(user.getId());
            return optionalUser.orElse(null);
        }
        return null;
    }

    public String getLoggedInUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails){
                return userDetails.getUsername();
            }
        }
        return null;
    }

}
