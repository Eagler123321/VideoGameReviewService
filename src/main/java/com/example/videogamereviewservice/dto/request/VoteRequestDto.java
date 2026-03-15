package com.example.videogamereviewservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Create a new vote")
public class VoteRequestDto {
    @Schema(description = "Type of vote ( Like / Dislike )", example = "Like")
    private String voteType;
    @Schema(description = "User id, FK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "Review id, FK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long reviewId;
}
