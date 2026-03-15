package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "gameId", target = "game.id")
    Review toEntity(ReviewRequestDto reviewRequestDto);
    
    ReviewResponseDto toDto(Review review);

    @Mapping(target = "id", ignore = true)
    void updateReviewFromDto(ReviewRequestDto dto, @MappingTarget Review entity);
}
         