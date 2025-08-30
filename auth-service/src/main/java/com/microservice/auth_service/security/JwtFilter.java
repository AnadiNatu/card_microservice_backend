package com.microservice.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader , "Bearer ")){
            filterChain.doFilter(request , response);
            return;
        }
        jwt = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);
        if ((!StringUtils.isEmpty(username)) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwt ,userDetails)){
                SecurityContext context = SecurityContextHolder.createEmptyContext();
//               But in your JwtFilter, you set credentials to null: to solve this Update your filter to include token in credentials:
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails , jwt , userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request , response);
    }
}
