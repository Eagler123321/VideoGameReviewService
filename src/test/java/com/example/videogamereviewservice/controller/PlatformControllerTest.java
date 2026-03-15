package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.request.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.PlatformServiceLocal;
import org.hamcrest.CoreMatchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlatformController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PlatformControllerTest {
    @MockitoBean
    private PlatformServiceLocal platformServiceLocal;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String name = "Android";
    private final Long platformId = 1L;

    private PlatformResponseDto platformResponseDto;
    private PlatformRequestDto platformRequestDto;

    @BeforeEach // ИТОГО 11 тестов
    public void init(){
        platformRequestDto = PlatformRequestDto.builder().name(name).build();

        platformResponseDto = PlatformResponseDto.builder().id(platformId).name(name).build();
    }

    @Test
    public void createPlatform_whenPlatformIsCreated_thenReturnsCreated() throws Exception{
        given(platformServiceLocal.createPlatform(any(PlatformRequestDto.class)))
                .willReturn(platformResponseDto);

        ResultActions response = mockMvc.perform(post("/platforms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platformRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(platformRequestDto.getName())));
    }

    @Test
    public void createPlatform_whenNameMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(post("/platforms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getPlatformById_whenExists_thenReturnsOk() throws Exception{
        when(platformServiceLocal.getPlatformById(platformId))
                .thenReturn(platformResponseDto);

        ResultActions response = mockMvc.perform(get("/platforms/{id}", platformId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(platformRequestDto.getName())));

    }

    @Test // Проверка исключения и статуса
    public void getPlatformById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(platformServiceLocal.getPlatformById(platformId))
                .thenThrow(new NotFoundException("Platform not found!"));

        ResultActions response = mockMvc.perform(get("/platforms/{id}", platformId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getPlatforms_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<PlatformResponseDto> genreResponseDtoList = new ArrayList<>(List.of(platformResponseDto));

        when(platformServiceLocal.getPlatforms())
                .thenReturn(genreResponseDtoList);

        ResultActions response = mockMvc.perform(get("/platforms")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(platformResponseDto.getName()));
    }

    @Test
    public void getPlatforms_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(platformServiceLocal.getPlatforms()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/platforms")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deletePlatformById_whenExists_thenReturnsOk() throws Exception{
        doNothing().when(platformServiceLocal).deletePlatformById(platformId);

        ResultActions response = mockMvc.perform(delete("/platforms/{id}", platformId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса
    public void deletePlatformById_whenPlatformNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Platform not found!"))
                .when(platformServiceLocal).deletePlatformById(999L);

        ResultActions response = mockMvc.perform(delete("/platforms/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updatePlatformById_whenValidRequest_thenReturnsOk() throws Exception{
        when(platformServiceLocal.updatePlatformById(any(PlatformRequestDto.class), eq(platformId)))
                .thenReturn(platformResponseDto);

        ResultActions response = mockMvc.perform(put("/platforms/{id}", platformId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platformRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(platformRequestDto.getName())));
    }

    @Test // Проверка исключения и статуса
    public void updatePlatformById_whenPlatformIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(platformServiceLocal.updatePlatformById(any(PlatformRequestDto.class), eq(platformId)))
                .willThrow(new NotFoundException("Platform not found!"));

        String validJson = """
            {
                "name":"android"
            }
            """;

        ResultActions response = mockMvc.perform(put("/platforms/{id}", platformId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updatePlatformById_whenNameIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(put("/platforms/{id}", platformId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }
}
