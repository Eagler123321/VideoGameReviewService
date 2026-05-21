package com.example.videogamereviewservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT токены аутентификации")
public class JwtAuthenticationDto {
    @Schema(description = "Access token (срок жизни: 15 минут)",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJrb3JhbDIyODE3IiwiaWF0IjoxNzA1MzE0NjAwLCJleHAiOjE3MDUzMTU1MDB9.abc123")
    private String token;

    @Schema(description = "Refresh token (срок жизни: 7 дней)",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJrb3JhbDIyODE3IiwiaWF0IjoxNzA1MzE0NjAwLCJleHAiOjE3MDU5MTk0MDB9.xyz789")
    private String refreshToken;
}
