package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.controller.base.VoteController;
import com.example.videogamereviewservice.dto.request.base.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.error.InvalidIdException;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.security.CustomUserServiceLocal;
import com.example.videogamereviewservice.security.jwt.JwtFilter;
import com.example.videogamereviewservice.security.jwt.JwtServiceLocal;
import com.example.videogamereviewservice.service.local.VoteServiceLocal;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private VoteServiceLocal voteServiceLocal;
    @MockitoBean
    private JwtFilter jwtFilter;
    @MockitoBean
    private JwtServiceLocal jwtServiceLocal;
    @MockitoBean
    private CustomUserServiceLocal customUserServiceLocal;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Long voteId = 1L;
    private final Long reviewId = 2L;
    private final Long userId = 3L;
    private final String voteType = "Like";
    private final LocalDateTime createdAt = LocalDateTime.of(2025, 5, 23, 15, 24,47);
    private VoteRequestDto voteRequestDto;
    private VoteResponseDto voteResponseDto;

    @BeforeEach // ИТОГО 13 тестов
    public void init(){
        voteRequestDto = VoteRequestDto.builder()
                .userId(userId)
                .reviewId(reviewId)
                .voteType(voteType)
                .build();
        voteResponseDto = VoteResponseDto.builder()
                .id(voteId)
                .createdAt(createdAt)
                .userId(userId)
                .reviewId(reviewId)
                .voteType(voteType)
                .build();
    }

    @Test
    public void createVote_whenVoteIsCreated_thenReturnsCreated() throws Exception{
        given(voteServiceLocal.createVote(any(VoteRequestDto.class)))
                .willReturn(voteResponseDto);

        ResultActions response = mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(voteId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.reviewId").value(reviewId))
                .andExpect(jsonPath("$.voteType").value(voteType));
    }

    @Test
    public void createVote_whenVoteTypeMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "userId":"3",
                "reviewId":"8"
            }
            """;

        ResultActions response = mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void createVote_whenReviewIdDoesNotExist_thenReturnsBadRequest() throws Exception {
        given(voteServiceLocal.createVote(any(VoteRequestDto.class)))
                .willThrow(new InvalidIdException("Review with id 999 does not exist"));
        // Json здесь декоративный, тест предназначен для проверки исключения InvalidIdException и статуса Bad Request
        String jsonWithInvalidReviewId = """
            {
                "voteType":"Like",
                "userId":"3",
                "reviewId":"999"
            }
            """;

        ResultActions response = mockMvc.perform(post("/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithInvalidReviewId));

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getVoteById_whenExists_thenReturnsOk() throws Exception{
        when(voteServiceLocal.getVoteById(voteId))
                .thenReturn(voteResponseDto);

        ResultActions response = mockMvc.perform(get("/votes/{id}", voteId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voteId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.reviewId").value(reviewId))
                .andExpect(jsonPath("$.voteType").value(voteType));
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void getVoteById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(voteServiceLocal.getVoteById(voteId))
                .thenThrow(new NotFoundException("Vote not found!"));

        ResultActions response = mockMvc.perform(get("/votes/{id}", voteId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getVotes_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<VoteResponseDto> voteResponseDtoList = new ArrayList<>(List.of(voteResponseDto));

        when(voteServiceLocal.getVotes()).thenReturn(voteResponseDtoList);

        ResultActions response = mockMvc.perform(get("/votes")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(voteId))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].reviewId").value(reviewId))
                .andExpect(jsonPath("$[0].voteType").value(voteType));
    }

    @Test
    public void getVotes_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(voteServiceLocal.getVotes()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/votes")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteVoteById_whenExists_thenReturnsNoContent() throws Exception{
        doNothing().when(voteServiceLocal).deleteVoteById(voteId);

        ResultActions response = mockMvc.perform(delete("/votes/{id}", voteId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void deleteVoteById_whenVoteNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Vote not found!"))
                .when(voteServiceLocal).deleteVoteById(999L);

        ResultActions response = mockMvc.perform(delete("/votes/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateVoteById_whenValidRequest_thenReturnsOk() throws Exception{
        when(voteServiceLocal.updateVoteById(any(VoteRequestDto.class), eq(voteId)))
                .thenReturn(voteResponseDto);

        ResultActions response = mockMvc.perform(put("/votes/{id}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voteId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.reviewId").value(reviewId))
                .andExpect(jsonPath("$.voteType").value(voteType));
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void updateVoteById_whenReviewIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(voteServiceLocal.updateVoteById(any(VoteRequestDto.class), eq(voteId)))
                .willThrow(new NotFoundException("Vote not found!"));

        String validJson = """
            {
                "voteType":"Like",
                "userId":"3",
                "reviewId":"2"
            }
            """;

        ResultActions response = mockMvc.perform(put("/votes/{id}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateVoteById_whenVoteTypeIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "userId":"3",
                "reviewId":"999"
            }
            """;

        ResultActions response = mockMvc.perform(put("/votes/{id}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }

    @Test // Проверка исключения и статуса (декоративный)
    public void updateVoteById_whenReviewIdDoesNotExist_thenReturnsBadRequest() throws Exception{
        given(voteServiceLocal.updateVoteById(any(VoteRequestDto.class), eq(voteId)))
                .willThrow(new InvalidIdException("Review with id 999 does not exist"));

        // Json здесь декоративный, тест предназначен для проверки исключения InvalidIdException и статуса Bad Request
        String jsonWithInvalidReviewId = """
            {
                "voteType":"Like",
                "userId":"3",
                "reviewId":"999"
            }
            """;

        ResultActions response = mockMvc.perform(put("/votes/{id}", voteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithInvalidReviewId));

        response.andExpect(status().isBadRequest());
    }
}
