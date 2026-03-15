package com.example.videogamereviewservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new tag")
public class TagResponseDto {
    @Schema(description = "Unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = "Name of tag", example = "cat", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
