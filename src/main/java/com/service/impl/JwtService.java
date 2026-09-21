package com.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final String key = "JwtKeyForBugTracterProjectApplication121212";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

    public String generateToken(String email){

        String token = Jwts.builder().subject(email)
                .issuedAt(new Date()).expiration( new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey).compact();

        return token;
    }

    public Claims getClaims(String token){
          return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token){

        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String email = extractEmail(token);

        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
}
