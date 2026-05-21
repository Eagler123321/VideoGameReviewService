package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.base.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.entity.Platform;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PlatformMapper {
    Platform toEntity(PlatformRequestDto platformRequestDto);

    PlatformResponseDto toDto(Platform platform);

    @Mapping(target = "id", ignore = true)
    void updatePlatformFromDto(PlatformRequestDto dto, @MappingTarget Platform entity);
}
