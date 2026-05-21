package com.example.videogamereviewservice.security.jwt;

import com.example.videogamereviewservice.dto.auth.JwtAuthenticationDto;

public interface JwtService {
    JwtAuthenticationDto generateAuthToken(String username);

    JwtAuthenticationDto refreshBaseToken(String username, String refreshToken);

    String getUsernameFromToken(String token);

    boolean validateJwtToken(String token);

    String generateJwtToken(String username);

    String generateRefreshToken(String username);
}
