package com.example.videogamereviewservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new user")
public class UserResponseDto {
    @Schema(description = "Unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = "User display name", example = "Eagler")
    private String nickname;
    @Email(message = "Invalid email format")
    @Schema(description = "Email address for notifications", example = "daniila@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "Nice image for user (avatar)", example = "https://example.com/avatar.png")
    private String avatarUrl;
    @Schema(description = "Only YYYY.MM.DD", example = "1985-09-13T12:00:00", requiredMode = Schema.RequiredMode.AUTO)
    private LocalDateTime registeredAt;
    @Schema(description = "Access rights (or permissions)", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;
    @Schema(description = "Description of user profile", example = "This is a sample user description.")
    private String description;
}
