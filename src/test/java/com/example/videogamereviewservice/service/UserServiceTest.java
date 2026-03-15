package com.example.videogamereviewservice.service;

import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.mapper.UserMapper;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.service.local.UserServiceLocal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceLocal userServiceLocal;

    private final Long userId = 1L;
    private final Long userId2 = 2L;
    private final String nickname = "Eagler";
    private final String nickname2 = "Decker";
    private final String username = "krol-makarol1488";
    private final String username2 = "lobada-rola228";
    private final String password = "jsuuvje123kai";
    private final String password2 = "asx2ojkaosx124";
    private final String email = "kirilka321@gmail.com";
    private final String email2 = "kira164@mail.ru";
    private final String avatarUrl = "sacefx.jpg";
    private final String avatarUrl2 = "dfscgkicj.jpg";
    private final LocalDateTime registeredAt = LocalDateTime.of(2025, 3, 16, 10, 35, 15);
    private final LocalDateTime registeredAt2 = LocalDateTime.of(2024, 1, 24, 1, 24, 54);;
    private final String role = "USER";
    private final String role2 = "USER";
    private final String description = "Cool bruh";
    private final String description2 = "Fool bruh";

    private UserResponseDto userResponseDto;
    private UserResponseDto userResponseDto2;

    private UserRequestDto userRequestDto;
    private UserRequestDto userRequestDto2;

    @BeforeEach
    public void init(){
        userResponseDto = UserResponseDto.builder()
                .id(userId)
                .nickname(nickname)
                .role(role)
                .email(email)
                .registeredAt(registeredAt)
                .avatarUrl(avatarUrl)
                .description(description)
                .build();
        userResponseDto2 = UserResponseDto.builder()
                .id(userId2)
                .nickname(nickname2)
                .role(role2)
                .email(email2)
                .registeredAt(registeredAt2)
                .avatarUrl(avatarUrl2)
                .description(description2)
                .build();

        userRequestDto = UserRequestDto.builder()
                .nickname(nickname)
                .role(role)
                .email(email)
                .password(password)
                .username(username)
                .avatarUrl(avatarUrl)
                .description(description)
                .build();

        userRequestDto2 = UserRequestDto.builder()
                .nickname(nickname2)
                .role(role2)
                .email(email2)
                .password(password2)
                .username(username2)
                .avatarUrl(avatarUrl2)
                .description(description2)
                .build();
    }
}
