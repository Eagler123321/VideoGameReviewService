package com.example.videogamereviewservice.dto.request.base;

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
public class GameRequestDto {
    @NotBlank(message = "Title is required")
    @Schema(description = "Title of game (full name)", example = "Nyan Cat")
    private String title;
    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed description of game", example = "Sweety games")
    private String description;
    @NotBlank(message = "ImageUrl is required")
    @Schema(description = "Nice image of game", example = "clicker")
    private String imageUrl;
    @Schema(description = "Only YYYY.MM.DD", example = "1985-09-13T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime releaseDate;
    @NotBlank(message = "Developer is required")
    @Schema(description = "Full name of developer", example = "Nintendo")
    private String developer;
    @NotBlank(message = "Publisher is required")
    @Schema(description = "Full name of publisher", example = "Nintendo")
    private String publisher;
    @Schema(description = "Maybe empty, FK", example = "[1]")
    private List<Long> tagIds;
    @Schema(description = "Not empty, FK", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> genreIds;
    @Schema(description = "Not empty, FK", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> platformIds;
}
