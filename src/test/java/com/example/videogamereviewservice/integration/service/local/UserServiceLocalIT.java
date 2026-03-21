package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.TagRequestDto;
import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.UserServiceLocal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
class UserServiceLocalIT {
    @Autowired
    private UserServiceLocal userServiceLocal;

    private static UserRequestDto createUserRequestDto(String name) {
        return UserRequestDto.builder()
                .username("pro228")
                .password("kias234")
                .email("makaron@gmail.com")
                .role("USER")
                .description("LET'S GO")
                .nickname(name)
                .avatarUrl("CSDFC.jpg")
                .build();
    }

    @Test
    void createUser_whenValidData_thenReturnsSavedUser() {
        UserRequestDto requestDto = createUserRequestDto("pRo!");

        UserResponseDto savedUser = userServiceLocal.createUser(requestDto);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getNickname()).isEqualTo("pRo!");
        assertThat(savedUser.getRole()).isEqualTo("USER");
        assertThat(savedUser.getAvatarUrl()).isEqualTo("CSDFC.jpg");
        assertThat(savedUser.getDescription()).isEqualTo("LET'S GO");
        assertThat(savedUser.getEmail()).isEqualTo("makaron@gmail.com");
    }

    @Test
    void getUserById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> userServiceLocal.getUserById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
