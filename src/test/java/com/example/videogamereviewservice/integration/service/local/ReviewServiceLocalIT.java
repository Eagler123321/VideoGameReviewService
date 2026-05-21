package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.base.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.service.local.ReviewServiceLocal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
class ReviewServiceLocalIT {
    @Autowired
    private ReviewServiceLocal reviewServiceLocal;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private User testUser;
    private Game testGame;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();
        testUser = userRepository.save(testUser);

        testGame = Game.builder()
                .title("Test Game")
                .description("Test")
                .developer("Dev")
                .publisher("Pub")
                .releaseDate(LocalDateTime.now())
                .build();
        testGame = gameRepository.save(testGame);
    }

    private static ReviewRequestDto createReviewRequestDto(String comment) {
        return ReviewRequestDto.builder()
                .comment(comment)
                .storyRate(4.)
                .graphicsRate(2.)
                .atmosphereRate(7.)
                .gameplayRate(5.)
                .build();
    }

    @Test
    void createReview_whenValidData_thenReturnsSavedReview() {
        ReviewRequestDto requestDto = createReviewRequestDto("cool jrpg game");

        ReviewResponseDto savedReview = reviewServiceLocal.createReview(requestDto);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getComment()).isEqualTo("cool jrpg game");
        assertThat(savedReview.getStoryRate()).isEqualTo(4.);
        assertThat(savedReview.getAtmosphereRate()).isEqualTo(7.);
        assertThat(savedReview.getGameplayRate()).isEqualTo(5.);
        assertThat(savedReview.getGraphicsRate()).isEqualTo(2.);
        assertThat(savedReview.getUserId()).isEqualTo(testUser.getId());
        assertThat(savedReview.getGameId()).isEqualTo(testGame.getId());
    }

    @Test
    void getReviewById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> reviewServiceLocal.getReviewById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
