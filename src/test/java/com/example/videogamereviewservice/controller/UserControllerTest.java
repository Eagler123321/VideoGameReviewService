package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.UserServiceLocal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserServiceLocal userServiceLocal;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;

    private final Long userId = 1L;
    private final String nickname = "Eagler";
    private final String username = "Eagler228";
    private final String password = "228339666";
    private final String email = "kira228@gmail.com";
    private final String role = "reviewer";
    private final String description = "Nice Trick Shot, bruuuh...";
    private final String avatarUrl = "asdjanjiwjdn.jpg";
    private final LocalDateTime registeredAt = LocalDateTime.now();

    @BeforeEach // ИТОГО 11 тестов
    public void init(){
        userResponseDto = UserResponseDto.builder()
                .id(userId)
                .nickname(nickname)
                .role(role)
                .avatarUrl(avatarUrl)
                .description(description)
                .email(email)
                .registeredAt(registeredAt)
                .build();

        userRequestDto = UserRequestDto.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .description(description)
                .avatarUrl(avatarUrl)
                .email(email)
                .role(role)
                .build();
    }

    private void assertUserResponse(ResultActions result) throws Exception {
        result.andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value(role))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.avatarUrl").value(avatarUrl));
    }

    @Test
    public void createUser_whenUserIsCreated_thenReturnsCreated() throws Exception{
        given(userServiceLocal.createUser(any(UserRequestDto.class)))
                .willReturn(userResponseDto);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));

        response.andExpect(status().isCreated());
        assertUserResponse(response);
    }

    @Test
    public void createUser_whenPasswordMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "nickname":"Eagler",
                "username":"koral22817",
                "email":"daniila@example.com",
                "avatarUrl":"https://example.com/avatar.png",
                "role":"USER",
                "description":"This is a sample user description."
            }
            """;

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getUserById_whenExists_thenReturnsOk() throws Exception{
        when(userServiceLocal.getUserById(userId))
                .thenReturn(userResponseDto);

        ResultActions response = mockMvc.perform(get("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
        assertUserResponse(response);
    }

    @Test // Проверка исключения и статуса
    public void getUserById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(userServiceLocal.getUserById(userId))
                .thenThrow(new NotFoundException("User not found!"));

        ResultActions response = mockMvc.perform(get("/users/{id}", userId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getUsers_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<UserResponseDto> userResponseDtoList = new ArrayList<>(List.of(userResponseDto));

        when(userServiceLocal.getUsers()).thenReturn(userResponseDtoList);

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(userId))
                .andExpect(jsonPath("$[0].nickname").value(nickname))
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[0].role").value(role))
                .andExpect(jsonPath("$[0].description").value(description))
                .andExpect(jsonPath("$[0].avatarUrl").value(avatarUrl));
    }

    @Test
    public void getUsers_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(userServiceLocal.getUsers()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteUserById_whenExists_thenReturnsNoContent() throws Exception{
        doNothing().when(userServiceLocal).deleteUserById(userId);

        ResultActions response = mockMvc.perform(delete("/users/{id}", userId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса
    public void deleteUserById_whenUserNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("User not found!"))
                .when(userServiceLocal).deleteUserById(999L);

        ResultActions response = mockMvc.perform(delete("/users/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateUserById_whenValidRequest_thenReturnsOk() throws Exception{
        when(userServiceLocal.updateUserById(any(UserRequestDto.class), eq(userId)))
                .thenReturn(userResponseDto);

        ResultActions response = mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value(role))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.avatarUrl").value(avatarUrl));
    }

    @Test // Проверка исключения и статуса
    public void updateUserById_whenUserIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(userServiceLocal.updateUserById(any(UserRequestDto.class), eq(userId)))
                .willThrow(new NotFoundException("User not found!"));

        String validJson = """
            {
                "password":"123123",
                "nickname":"Eagler",
                "username":"koral22817",
                "email":"daniila@example.com",
                "avatarUrl":"https://example.com/avatar.png",
                "role":"USER",
                "description":"This is a sample user description."
            }
            """;

        ResultActions response = mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateUserById_whenPasswordIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "nickname":"Eagler",
                "username":"koral22817",
                "email":"daniila@example.com",
                "avatarUrl":"https://example.com/avatar.png",
                "role":"USER",
                "description":"This is a sample user description."
            }
            """;

        ResultActions response = mockMvc.perform(put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }
}
