package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.base.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TagMapper {
    Tag toEntity(TagRequestDto tagRequestDto);

    TagResponseDto toDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    void updateTagFromDto(TagRequestDto dto, @MappingTarget Tag entity);
}
