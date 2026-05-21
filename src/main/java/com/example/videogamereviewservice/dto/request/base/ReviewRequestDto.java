package com.example.videogamereviewservice.dto.request.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new review")
public class ReviewRequestDto {
    @NotBlank(message = "Comment is required")
    @Schema(description = "Detailed review comment", example = "This is a nice and cute game")
    private String comment;
    @Schema(description = "A rate of game story (1-10)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double storyRate;
    @Schema(description = "A rate of game play (1-10)", example = "8", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double gameplayRate;
    @Schema(description = "A rate of game graphic (1-10)", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double graphicsRate;
    @Schema(description = "A rate of game atmosphere (1-10)", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double atmosphereRate;
    @Schema(description = "Game id, FK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long gameId;
    @Schema(description = "User id, FK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
}
