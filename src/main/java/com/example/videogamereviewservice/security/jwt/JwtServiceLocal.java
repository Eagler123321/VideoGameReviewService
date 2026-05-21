package com.example.videogamereviewservice.security.jwt;

import com.example.videogamereviewservice.dto.auth.JwtAuthenticationDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtServiceLocal implements JwtService{
    private static final Logger LOGGER = LogManager.getLogger(JwtServiceLocal.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    public JwtAuthenticationDto generateAuthToken(String username){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken){
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        }catch (UnsupportedJwtException unsEx){
            LOGGER.error("Unsupported JwtException");
        }catch (MalformedJwtException malEx){
            LOGGER.error("MalformedJwtException JwtException", malEx);
        }catch (SecurityException secEx){
            LOGGER.error("Security Exception", secEx);
        }catch (Exception exExp){
            LOGGER.error("Invalid token", exExp);
        }
        return false;
    }

    public String generateJwtToken(String username){
        Date date = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    public String generateRefreshToken(String username){
        Date date = Date.from(LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(username)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}