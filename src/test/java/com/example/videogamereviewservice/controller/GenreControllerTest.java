package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.GenreServiceLocal;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GenreController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GenreControllerTest {
    @MockitoBean
    private GenreServiceLocal genreServiceLocal;
    
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String name = "RPG";
    private final Long genreId = 1L;

    private GenreResponseDto genreResponseDto;
    private GenreRequestDto genreRequestDto;

    @BeforeEach // ИТОГО 11 тестов
    public void init(){
        genreRequestDto = GenreRequestDto.builder().name(name).build();

        genreResponseDto = GenreResponseDto.builder().id(genreId).name(name).build();
    }

    @Test
    public void createGenre_whenGenreIsCreated_thenReturnsCreated() throws Exception{
        given(genreServiceLocal.createGenre(any(GenreRequestDto.class)))
                .willReturn(genreResponseDto);

        ResultActions response = mockMvc.perform(post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(genreRequestDto.getName())));
    }

    @Test
    public void createGenre_whenNameMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getGenreById_whenExists_thenReturnsOk() throws Exception{
        when(genreServiceLocal.getGenreById(genreId))
                .thenReturn(genreResponseDto);

        ResultActions response = mockMvc.perform(get("/genres/{id}", genreId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(genreRequestDto.getName())));

    }

    @Test // Проверка исключения и статуса (декоративный)
    public void getGenreById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(genreServiceLocal.getGenreById(genreId))
                .thenThrow(new NotFoundException("Genre not found!"));

        ResultActions response = mockMvc.perform(get("/genres/{id}", genreId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getGenres_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<GenreResponseDto> genreResponseDtoList = new ArrayList<>(List.of(genreResponseDto));

        when(genreServiceLocal.getGenres())
                .thenReturn(genreResponseDtoList);

        ResultActions response = mockMvc.perform(get("/genres")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(genreResponseDto.getName()));
    }

    @Test
    public void getGenres_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(genreServiceLocal.getGenres()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/genres")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteGenreById_whenExists_thenReturnNoContent() throws Exception{
        doNothing().when(genreServiceLocal).deleteGenreById(genreId);

        ResultActions response = mockMvc.perform(delete("/genres/{id}", genreId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void deleteGenreById_whenGenreNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Genre not found!"))
                .when(genreServiceLocal).deleteGenreById(999L);

        ResultActions response = mockMvc.perform(delete("/genres/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateGenreById_whenValidRequest_thenReturnOk() throws Exception{
        when(genreServiceLocal.updateGenreById(any(GenreRequestDto.class), eq(genreId)))
                .thenReturn(genreResponseDto);

        ResultActions response = mockMvc.perform(put("/genres/{id}", genreId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(genreRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(genreRequestDto.getName())));
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void updateGenreById_whenGenreIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(genreServiceLocal.updateGenreById(any(GenreRequestDto.class), eq(genreId)))
                .willThrow(new NotFoundException("Genre not found!"));

        String validJson = """
            {
                "name":"rpg"
            }
            """;

        ResultActions response = mockMvc.perform(put("/genres/{id}", genreId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateGenreById_whenNameIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(put("/genres/{id}", genreId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }
}
