package com.example.videogamereviewservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Refresh token для обновления сессии")
public class RefreshTokenDto {
    @Schema(description = "Refresh token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJza2liaWRpZ2FtZXI2OSIsImlhdCI6MTYxNjIzOTAyMiwiZXhwIjoxNjE2ODQzODIyfQ.xyz789",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}
