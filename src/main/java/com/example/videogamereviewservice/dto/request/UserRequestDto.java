package com.example.videogamereviewservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @Schema(description = "Unique username (login)", example = "koral22817")
    private String username;
    @NotBlank(message = "Password is required")
    @Schema(description = "Only letters, numbers", example = "password123")
    private String password;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(description = "Email address for notifications", example = "daniila@example.com")
    private String email;
    @Schema(description = "Nice image for user (avatar)", example = "https://example.com/avatar.png")
    private String avatarUrl;
    @NotBlank(message = "Role is required")
    @Schema(description = "Access rights (or permissions)", example = "USER")
    private String role;
    @Schema(description = "Description of user profile", example = "This is a sample user description.")
    private String description;
}
