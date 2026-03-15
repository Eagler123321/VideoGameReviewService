package com.example.videogamereviewservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new game")
public class GameResponseDto {
    @Schema(description = "Unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = "Title of game (full name)", example = "Nyan Cat", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "Detailed description of game", example = "Sweety games", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    @Schema(description = "Nice image of game", example = "clicker", requiredMode = Schema.RequiredMode.REQUIRED)
    private String imageUrl;
    @Schema(description = "Only YYYY.MM.DD", example = "1985-09-13T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime releaseDate;
    @Schema(description = "Full name of developer", example = "Nintendo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String developer;
    @Schema(description = "Full name of publisher", example = "Nintendo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String publisher;
    @Schema(description = "Maybe empty, FK", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> tagIds;
    @Schema(description = "Not empty, FK", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> genreIds;
    @Schema(description = "Not empty, FK", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> platformIds;
}
