package ru.glebdos.usermicroservice.config;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class JwtCore {

    @Value("${testing.app.secret}")
    private String secret;

    @Value("${testing.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication auth) {
        log.info("Generating token ");
        UserDetailsImpl userDetails  = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getFirstName()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + lifetime))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String getNameFromJwtToken(String token) {
        log.info("getNameFromJwtToken");

        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
    }
}
