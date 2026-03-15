package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toEntity(GenreRequestDto genreRequestDto);

    GenreResponseDto toDto(Genre genre);

    @Mapping(target = "id", ignore = true)
    void updateGenreFromDto(GenreRequestDto dto, @MappingTarget Genre entity);
}
