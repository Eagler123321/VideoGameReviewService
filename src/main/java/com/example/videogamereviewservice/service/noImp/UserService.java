package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.auth.JwtAuthenticationDto;
import com.example.videogamereviewservice.dto.auth.RefreshTokenDto;
import com.example.videogamereviewservice.dto.auth.UserCredentialsDto;
import com.example.videogamereviewservice.dto.request.base.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;

import javax.naming.AuthenticationException;
import java.util.List;

public interface UserService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
    
    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUserById(UserRequestDto userRequestDto, Long id);

    UserResponseDto getUserById(Long id);

    User findUserByEmail(String email) throws Exception;

    List<UserResponseDto> getUsers();

    void deleteUserById(Long id);

}
