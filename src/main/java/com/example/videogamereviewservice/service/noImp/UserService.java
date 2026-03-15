package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUserById(UserRequestDto userRequestDto, Long id);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getUsers();

    void deleteUserById(Long id);

}
