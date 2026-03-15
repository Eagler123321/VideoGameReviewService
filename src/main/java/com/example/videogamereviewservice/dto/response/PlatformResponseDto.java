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
@Schema(description = "Create a new platform")
public class PlatformResponseDto {
    @Schema(description = "Unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    @Schema(description = "Name of platform", example = "android", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
