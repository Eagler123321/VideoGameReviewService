package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    // Straight Mapping
    VoteResponseDto toDto(Vote vote);
    // Reverse Mapping
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "reviewId", target = "review.id")
    Vote toEntity(VoteRequestDto voteRequestDto);
    // Put Mapping
    @Mapping(target = "id", ignore = true)
    void updateVoteFromDto(VoteRequestDto voteRequestDto, @MappingTarget Vote vote);
}
