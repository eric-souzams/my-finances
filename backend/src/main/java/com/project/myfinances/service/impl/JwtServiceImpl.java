package com.project.myfinances.service.impl;

import com.project.myfinances.model.entity.Usuario;
import com.project.myfinances.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.key-signature}")
    private String keySignature;

    @Override
    public String generateToken(Usuario user) {
        long expirationTime = Long.parseLong(expiration);
        LocalDateTime dateTimeExpiration = LocalDateTime.now().plusMinutes(expirationTime);

        Instant instant = dateTimeExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date dateExpiration = Date.from(instant);

        log.info("New token generated to user: {}", user.getEmail());
        return Jwts.builder()
                .setExpiration(dateExpiration)
                .setSubject(user.getEmail())
                .claim("userName", user.getNome())
                .claim("userId", user.getId())
                .signWith(SignatureAlgorithm.HS512, keySignature)
                .compact();
    }

    @Override
    public Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(keySignature)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();
            LocalDateTime dateTimeExpiration = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            boolean isAfter = LocalDateTime.now().isAfter(dateTimeExpiration);

            return !isAfter;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    @Override
    public String getUserLogin(String token) {
        Claims claims = getClaims(token);

        return claims.getSubject();
    }
}
