package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.controller.base.GameController;
import com.example.videogamereviewservice.dto.request.base.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.error.InvalidIdException;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.security.CustomUserServiceLocal;
import com.example.videogamereviewservice.security.jwt.JwtFilter;
import com.example.videogamereviewservice.security.jwt.JwtServiceLocal;
import com.example.videogamereviewservice.service.local.GameServiceLocal;
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

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private GameServiceLocal gameServiceLocal;
    @MockitoBean
    private JwtFilter jwtFilter;
    @MockitoBean
    private JwtServiceLocal jwtServiceLocal;
    @MockitoBean
    private CustomUserServiceLocal customUserServiceLocal;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private GameResponseDto gameResponseDto;
    private GameRequestDto gameRequestDto;

    private static final Long gameId = 1L;
    private static final String title = "Tekken 7";
    private static final String description = "Cool fighting";
    private static final String imageUrl = "asjidnaijsd.jpg";
    private static final String developer = "Bandai Namco Entertainment";
    private static final String publisher = "Bandai Namco Entertainment";
    private static final List<Long> genreIds = List.of(1L, 2L, 3L, 1L);
    private static final List<Long> platformIds = List.of(2L, 3L);
    private static final List<Long> tagIds = List.of(1L, 3L);

    @BeforeEach // ИТОГО 15 тестов (44 + 26 + 15) + 35 +  +  = 50 + 50 + 20 = 120
    public void init(){
        gameRequestDto = GameRequestDto.builder()
                .description(description)
                .developer(developer)
                .publisher(publisher)
                .title(title)
                .imageUrl(imageUrl)
                .genreIds(genreIds)
                .platformIds(platformIds)
                .tagIds(tagIds)
                .build();

        gameResponseDto = GameResponseDto.builder()
                .id(gameId)
                .description(description)
                .developer(developer)
                .publisher(publisher)
                .title(title)
                .imageUrl(imageUrl)
                .genreIds(genreIds)
                .platformIds(platformIds)
                .tagIds(tagIds)
                .build();
    }

    private void assertGameResponse(ResultActions result) throws Exception {
        result.andExpect(jsonPath("$.id").value(gameId))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.imageUrl").value(imageUrl))
                .andExpect(jsonPath("$.developer").value(developer))
                .andExpect(jsonPath("$.publisher").value(publisher))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.genreIds", hasItems(1, 2, 3)))
                .andExpect(jsonPath("$.platformIds", hasItems(2, 3)))
                .andExpect(jsonPath("$.tagIds", hasItems(1, 3)));
    }

    @Test
    public void createGame_whenValidRequest_thenReturnsCreated() throws Exception{
        given(gameServiceLocal.createGame(any(GameRequestDto.class)))
                .willReturn(gameResponseDto);

        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameRequestDto)));

        response.andExpect(status().isCreated());
        assertGameResponse(response);
    }

    @Test
    public void createGame_whenTitleMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "description": "No title",
                "developer": "Test",
                "publisher": "Test",
                "releaseDate": "2024-01-01T00:00:00",
                "imageUrl": "test.jpg",
                "genreIds": [1],
                "platformIds": [1],
                "tagIds": [1]
            }
            """;
        ResultActions response = mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }

    @Test
    public void createGame_whenFkIdsIsRepeated_thenReturnsCreated() throws Exception{
        GameResponseDto expectedResponse = GameResponseDto.builder()
                .id(gameId)
                .title("title")
                .description("i'm good")
                .developer("Test")
                .publisher("Test")
                .genreIds(List.of(1L, 2L))
                .platformIds(List.of(1L, 2L))
                .tagIds(List.of(1L))
                .releaseDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .build();

        given(gameServiceLocal.createGame(any(GameRequestDto.class)))
                .willReturn(expectedResponse);

        String jsonWithDuplicateTagIds = """
            {
                "title":"title",
                "description": "i'm good",
                "developer": "Test",
                "publisher": "Test",
                "releaseDate": "2024-01-01T00:00:00",
                "imageUrl": "test.jpg",
                "genreIds": [1, 1, 2, 2],
                "platformIds": [1, 2, 2],
                "tagIds": [1, 1, 1]
            }
            """;

        ResultActions response = mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithDuplicateTagIds));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagIds", hasItems(1)))
                .andExpect(jsonPath("$.tagIds", hasSize(1)))
                .andExpect(jsonPath("$.genreIds", hasItems(1, 2)))
                .andExpect(jsonPath("$.genreIds", hasSize(2)))
                .andExpect(jsonPath("$.platformIds", hasItems(1, 2)))
                .andExpect(jsonPath("$.platformIds", hasSize(2)));
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void createGame_whenTagIdDoesNotExist_thenReturnsBadRequest() throws Exception {
        given(gameServiceLocal.createGame(any(GameRequestDto.class)))
                .willThrow(new InvalidIdException("Tag with id 999 does not exist"));
        // Json здесь декоративный, тест предназначен для проверки исключения InvalidIdException и статуса Bad Request
        String jsonWithInvalidTag = """
            {
                "title": "Test Game",
                "description": "Test",
                "developer": "Test",
                "publisher": "Test",
                "imageUrl":"asdasd.jpg",
                "releaseDate": "2024-01-01T00:00:00",
                "genreIds": [1],
                "platformIds": [1],
                "tagIds": [999]
            }
            """;

        ResultActions response = mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithInvalidTag));

        response.andExpect(status().isBadRequest());
    }
    
    @Test
    public void getGameById_whenExists_thenReturnsOk() throws Exception{
        when(gameServiceLocal.getGameById(gameId))
                .thenReturn(gameResponseDto);

        ResultActions response = mockMvc.perform(get("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
        assertGameResponse(response);
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void getGameById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(gameServiceLocal.getGameById(gameId))
                .thenThrow(new NotFoundException("Game not found!"));

        ResultActions response = mockMvc.perform(get("/games/{id}", gameId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getGames_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<GameResponseDto> gameResponseDtoList = new ArrayList<>(List.of(gameResponseDto));

        when(gameServiceLocal.getGames()).thenReturn(gameResponseDtoList);

        ResultActions response = mockMvc.perform(get("/games")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(gameId))
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].imageUrl").value(imageUrl))
                .andExpect(jsonPath("$[0].developer").value(developer))
                .andExpect(jsonPath("$[0].publisher").value(publisher))
                .andExpect(jsonPath("$[0].description").value(description))
                .andExpect(jsonPath("$[0].genreIds", hasItems(1, 2, 3)))
                .andExpect(jsonPath("$[0].platformIds", hasItems(2, 3)))
                .andExpect(jsonPath("$[0].tagIds", hasItems(1, 3)));
    }

    @Test
    public void getGames_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(gameServiceLocal.getGames()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/games")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteGameById_whenExists_thenReturnsNoContent() throws Exception{
        doNothing().when(gameServiceLocal).deleteGameById(gameId);

        ResultActions response = mockMvc.perform(delete("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void deleteGameById_whenGameNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Game not found!"))
                .when(gameServiceLocal).deleteGameById(999L);

        ResultActions response = mockMvc.perform(delete("/games/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateGameById_whenValidRequest_thenReturnsOk() throws Exception{
        when(gameServiceLocal.updateGameById(gameRequestDto, gameId))
                .thenReturn(gameResponseDto);

        ResultActions response = mockMvc.perform(put("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameRequestDto)));

        response.andExpect(status().isOk());
        assertGameResponse(response);
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void updateGameById_whenTagIdDoesNotExist_thenReturnsBadRequest() throws Exception{
        given(gameServiceLocal.updateGameById(any(GameRequestDto.class), eq(gameId)))
                .willThrow(new InvalidIdException("Tag with id 999 does not exist"));

        String jsonWithInvalidTag = """
            {
                "title":"title",
                "description": "No title",
                "developer": "Test",
                "publisher": "Test",
                "releaseDate": "2024-01-01T00:00:00",
                "imageUrl": "test.jpg",
                "genreIds": [1],
                "platformIds": [1],
                "tagIds": [999]
            }
            """;

        ResultActions response = mockMvc.perform(put("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithInvalidTag));

        response.andExpect(status().isBadRequest());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void updateGameById_whenGameIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(gameServiceLocal.updateGameById(any(GameRequestDto.class), eq(gameId)))
                .willThrow(new NotFoundException("Game not found!"));

        String validJson = """
            {
                "title":"title",
                "description": "No title",
                "developer": "Test",
                "publisher": "Test",
                "releaseDate": "2024-01-01T00:00:00",
                "imageUrl": "test.jpg",
                "genreIds": [1],
                "platformIds": [1],
                "tagIds": [999]
            }
            """;

        ResultActions response = mockMvc.perform(put("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateGameById_whenFkIdsIsRepeated_thenReturnsOk() throws Exception{
        GameResponseDto expectedResponse = GameResponseDto.builder()
                .id(gameId)
                .title("title")
                .description("i'm good")
                .developer("Test")
                .publisher("Test")
                .genreIds(List.of(1L, 2L))
                .platformIds(List.of(1L, 2L))
                .tagIds(List.of(1L))
                .releaseDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .build();

        when(gameServiceLocal.updateGameById(any(GameRequestDto.class), eq(gameId)))
                .thenReturn(expectedResponse);

        ResultActions response = mockMvc.perform(put("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.tagIds", hasItems(1)))
                .andExpect(jsonPath("$.tagIds", hasSize(1)))
                .andExpect(jsonPath("$.genreIds", hasItems(1, 2)))
                .andExpect(jsonPath("$.genreIds", hasSize(2)))
                .andExpect(jsonPath("$.platformIds", hasItems(1, 2)))
                .andExpect(jsonPath("$.platformIds", hasSize(2)));

    }

    @Test
    public void updateGameById_whenTitleIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "description": "No title",
                "developer": "Test",
                "publisher": "Test",
                "releaseDate": "2024-01-01T00:00:00",
                "imageUrl": "test.jpg",
                "genreIds": [1],
                "platformIds": [1],
                "tagIds": [1]
            }
            """;
        
        ResultActions response = mockMvc.perform(put("/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }
}
