package ru.glebdos.usermicroservice.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {



    private final JwtCore jwtCore;

    private final UserDetailsService userDetailsService;
    @Autowired
    public TokenFilter(JwtCore jwtCore, UserDetailsService userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("TokenFilter");
        String jwt = null;
        String email = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken authenticationToken = null;

        try{
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);

            }
            if (jwt != null) {
                try {
                    email = jwtCore.getNameFromJwtToken(jwt);
                }
                catch (ExpiredJwtException e){
                    log.error("JWT token expired: {}", e.getMessage(), e);
                }
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    userDetails = userDetailsService.loadUserByUsername(email);
                    authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,null);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("Authentication Success");
                }
            }

        } catch (Exception e){
            log.error("Error while parsing JWT token: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}
