package com.example.videogamereviewservice.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new user")
public class UserRequestDto {
    @Schema(description = "User display name", example = "Eagler")
    private String nickname;
    @NotBlank(message = "Username is required")
    @Schema(description = "Unique username (login)", example = "koral22817",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @NotBlank(message = "Password is required")
    @Schema(description = "Only letters, numbers", example = "Password123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain uppercase, lowercase, and digit")
    private String password;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address for notifications", example = "daniila@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "Nice image for user (avatar)", example = "https://example.com/avatar.png")
    private String avatarUrl;
    @Schema(description = "Description of user profile", example = "This is a sample user description.")
    private String description;
}
