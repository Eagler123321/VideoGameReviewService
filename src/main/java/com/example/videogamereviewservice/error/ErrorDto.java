package com.example.videogamereviewservice.error;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Error dto for response")
public class ErrorDto {
    @Schema(description = "HTTP Status", example = "404")
    private Integer status;
    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed error description", example = "User not found with id 5")
    private String description;
    @NotBlank(message = "Title is required")
    @Schema(description = "Reason phrase of HTTP Status", example = "Not found!")
    private String title;

    public ErrorDto(Integer status, String title, String description) {
        this.status = status;
        this.description = description;
        this.title = title;
    }
}
