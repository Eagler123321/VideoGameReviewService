package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.*;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.error.InvalidIdException;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.ReviewServiceLocal;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReviewServiceLocal reviewServiceLocal;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ReviewResponseDto reviewResponseDto;
    private ReviewRequestDto reviewRequestDto;


    private final Long reviewId = 1L;
    private final String comment = "Cool game!";
    private final LocalDateTime createdAt = LocalDateTime.now().plusDays(1);
    private final LocalDateTime updatedAt = LocalDateTime.now().plusDays(4);
    private final Double storyRate = 3.;
    private final Double gameplayRate = 7.;
    private final Double graphicsRate = 7.;
    private final Double atmosphereRate = 3.;
    private final Long gameId = 2L;
    private final Long userId = 3L;

    @BeforeEach // ИТОГО 13 тестов
    public void init(){
        reviewRequestDto = ReviewRequestDto.builder()
                .comment(comment)
                .atmosphereRate(atmosphereRate)
                .gameplayRate(gameplayRate)
                .graphicsRate(graphicsRate)
                .storyRate(storyRate)
                .gameId(gameId)
                .userId(userId)
                .build();

        reviewResponseDto = ReviewResponseDto.builder()
                .id(reviewId)
                .comment(comment)
                .atmosphereRate(atmosphereRate)
                .gameplayRate(gameplayRate)
                .graphicsRate(graphicsRate)
                .storyRate(storyRate)
                .gameId(gameId)
                .userId(userId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    public void createReview_whenReviewIsCreated_thenReturnsCreated() throws Exception{
        given(reviewServiceLocal.createReview(any(ReviewRequestDto.class)))
                .willReturn(reviewResponseDto);

        ResultActions response = mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.comment").value(comment))
                .andExpect(jsonPath("$.atmosphereRate").value(atmosphereRate))
                .andExpect(jsonPath("$.gameplayRate").value(gameplayRate))
                .andExpect(jsonPath("$.graphicsRate").value(graphicsRate))
                .andExpect(jsonPath("$.storyRate").value(storyRate))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gameId").value(gameId));
    }

    @Test
    public void createReview_whenCommentMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "storyRate":"3",
                "gameplayRate":"8",
                "graphicsRate":"7",
                "atmosphereRate":"10",
                "gameId":"1",
                "userId":"1"
            }
            """;

        ResultActions response = mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void createReview_whenGameIdDoesNotExist_thenReturnsBadRequest() throws Exception {
        given(reviewServiceLocal.createReview(any(ReviewRequestDto.class)))
                .willThrow(new InvalidIdException("Tag with id 999 does not exist"));
        // Json здесь декоративный, тест предназначен для проверки исключения InvalidIdException и статуса Bad Request
        String jsonWithInvalidTag = """
            {
                "storyRate":"3",
                "gameplayRate":"8",
                "graphicsRate":"7",
                "atmosphereRate":"10",
                "gameId":"1",
                "userId":"999"
            }
            """;

        ResultActions response = mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithInvalidTag));

        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getReviewById_whenExists_thenReturnsOk() throws Exception{
        when(reviewServiceLocal.getReviewById(reviewId))
                .thenReturn(reviewResponseDto);

        ResultActions response = mockMvc.perform(get("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.comment").value(comment))
                .andExpect(jsonPath("$.atmosphereRate").value(atmosphereRate))
                .andExpect(jsonPath("$.gameplayRate").value(gameplayRate))
                .andExpect(jsonPath("$.graphicsRate").value(graphicsRate))
                .andExpect(jsonPath("$.storyRate").value(storyRate))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gameId").value(gameId));
    }

    @Test // Проверка исключения и статуса
    public void getReviewById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(reviewServiceLocal.getReviewById(reviewId))
                .thenThrow(new NotFoundException("Review not found!"));

        ResultActions response = mockMvc.perform(get("/reviews/{id}", reviewId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getReviews_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>(List.of(reviewResponseDto));

        when(reviewServiceLocal.getReviews()).thenReturn(reviewResponseDtoList);

        ResultActions response = mockMvc.perform(get("/reviews")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reviewId))
                .andExpect(jsonPath("$[0].comment").value(comment))
                .andExpect(jsonPath("$[0].atmosphereRate").value(atmosphereRate))
                .andExpect(jsonPath("$[0].gameplayRate").value(gameplayRate))
                .andExpect(jsonPath("$[0].graphicsRate").value(graphicsRate))
                .andExpect(jsonPath("$[0].storyRate").value(storyRate))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].gameId").value(gameId));
    }

    @Test
    public void getReviews_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(reviewServiceLocal.getReviews()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/reviews")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteReviewById_whenExists_thenReturnsNoContent() throws Exception{
        doNothing().when(reviewServiceLocal).deleteReviewById(reviewId);

        ResultActions response = mockMvc.perform(delete("/reviews/{id}", reviewId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса
    public void deleteReviewById_whenReviewNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Review not found!"))
                .when(reviewServiceLocal).deleteReviewById(999L);

        ResultActions response = mockMvc.perform(delete("/reviews/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateReviewById_whenValidRequest_thenReturnsOk() throws Exception{
        when(reviewServiceLocal.updateReviewById(any(ReviewRequestDto.class), eq(reviewId)))
                .thenReturn(reviewResponseDto);

        ResultActions response = mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.comment").value(comment))
                .andExpect(jsonPath("$.atmosphereRate").value(atmosphereRate))
                .andExpect(jsonPath("$.gameplayRate").value(gameplayRate))
                .andExpect(jsonPath("$.graphicsRate").value(graphicsRate))
                .andExpect(jsonPath("$.storyRate").value(storyRate))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.gameId").value(gameId));
    }

    @Test // Проверка исключения и статуса
    public void updateReviewById_whenReviewIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(reviewServiceLocal.updateReviewById(any(ReviewRequestDto.class), eq(reviewId)))
                .willThrow(new NotFoundException("Review not found!"));

        String validJson = """
            {
                "comment":"Cool game",
                "storyRate":"3",
                "gameplayRate":"8",
                "graphicsRate":"7",
                "atmosphereRate":"10",
                "gameId":"1",
                "userId":"1"
            }
            """;

        ResultActions response = mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateReviewById_whenCommentIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
                "storyRate":"3",
                "gameplayRate":"8",
                "graphicsRate":"7",
                "atmosphereRate":"10",
                "gameId":"1",
                "userId":"1"
            }
            """;

        ResultActions response = mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }

    @Test // Проверка исключения и статуса
    public void updateReviewById_whenUserIdDoesNotExist_thenReturnsBadRequest() throws Exception{
        given(reviewServiceLocal.updateReviewById(any(ReviewRequestDto.class), eq(reviewId)))
                .willThrow(new InvalidIdException("User with id 999 does not exist"));

        // Json здесь декоративный, тест предназначен для проверки исключения InvalidIdException и статуса Bad Request
        String jsonWithInvalidTag = """
            {
                "storyRate":"3",
                "gameplayRate":"8",
                "graphicsRate":"7",
                "atmosphereRate":"10",
                "gameId":"1",
                "userId":"999"
            }
            """;

        ResultActions response = mockMvc.perform(put("/reviews/{id}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithInvalidTag));

        response.andExpect(status().isBadRequest());
    }
}
