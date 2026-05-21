package com.example.videogamereviewservice.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Учётные данные для входа")
public class UserCredentialsDto {
    @Schema(description = "Email или имя пользователя", example = "SkibidiGamer69", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "Пароль", example = "StrongPassword123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
