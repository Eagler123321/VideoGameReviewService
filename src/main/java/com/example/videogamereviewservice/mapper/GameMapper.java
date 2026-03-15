package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.Genre;
import com.example.videogamereviewservice.entity.Platform;
import com.example.videogamereviewservice.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface GameMapper {
    Game toEntity(GameRequestDto gameRequestDto);

    GameResponseDto toDto(Game game);

    @Mapping(target = "id", ignore = true)
    void updateGameFromDto(GameRequestDto dto, @MappingTarget Game entity);

    @Named("toIds")
    default <T> List<Long> toIds(List<T> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(e -> {
                    if (e instanceof Tag) return ((Tag) e).getId();
                    if (e instanceof Genre) return ((Genre) e).getId();
                    if (e instanceof Platform) return ((Platform) e).getId();
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
