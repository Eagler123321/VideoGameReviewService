package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.base.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserRequestDto dto, @MappingTarget User entity);
}
