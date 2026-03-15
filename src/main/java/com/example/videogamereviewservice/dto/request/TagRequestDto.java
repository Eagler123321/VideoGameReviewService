package com.example.videogamereviewservice.dto.request;

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
@Schema(description = "Create a new tag")
public class TagRequestDto {
    @NotBlank(message = "Name is required")
    @Schema(description = "Name of tag", example = "cat")
    private String name;
}

