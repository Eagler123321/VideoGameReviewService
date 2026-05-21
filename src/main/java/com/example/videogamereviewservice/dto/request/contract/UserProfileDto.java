package com.example.videogamereviewservice.dto.request.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "User Profile data")
public class UserProfileDto {
    @Schema(description = "User display name", example = "Eagler")
    private String nickname;
    @Schema(description = "Nice image for user (avatar)", example = "https://example.com/avatar.png")
    private String avatarUrl;
    @Schema(description = "Description of user profile", example = "This is a sample user description.")
    private String description;
}
