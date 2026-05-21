package com.example.videogamereviewservice.mapper;

import com.example.videogamereviewservice.dto.request.contract.UserProfileDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    User toEntity(UserProfileDto userProfileDto);

    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserProfileDto dto, @MappingTarget User entity);
}
