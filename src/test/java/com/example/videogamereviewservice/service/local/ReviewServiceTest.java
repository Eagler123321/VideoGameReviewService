package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.base.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.Genre;
import com.example.videogamereviewservice.entity.Review;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.InvalidIdException;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.ReviewMapper;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.repository.ReviewRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private ReviewServiceLocal reviewServiceLocal;

    private Review review;
    private Review review2;

    private final Long reviewId = 1L;
    private final Long reviewId2 = 2L;
    private final Long gameId = 3L;
    private final Long gameId2 = 4L;
    private final Long userId = 5L;
    private final Long userId2 = 6L;
    private final String comment = "nice game!";
    private final String comment2 = "beautiful game!";
    private final LocalDateTime createdAt = LocalDateTime.of(2020, 5, 10, 5, 3, 35);
    private final LocalDateTime createdAt2 = LocalDateTime.of(2023, 2, 3, 13, 43, 16);
    private final LocalDateTime updatedAt = LocalDateTime.of(2025, 10, 23, 10, 47, 55);
    private final LocalDateTime updatedAt2 = LocalDateTime.of(2024, 8, 13, 16, 1, 45);
    private final Double gameplayRate = 8.;
    private final Double gameplayRate2 = 9.;
    private final Double graphicsRate = 3.;
    private final Double graphicsRate2 = 4.;
    private final Double storyRate = 5.;
    private final Double storyRate2 = 6.;
    private final Double atmosphereRate = 10.;
    private final Double atmosphereRate2 = 7.;
    private ReviewResponseDto reviewResponseDto;
    private ReviewResponseDto reviewResponseDto2;
    private ReviewRequestDto reviewRequestDto;
    private ReviewRequestDto reviewRequestDto2;
    private Game game;
    private User user;

    @BeforeEach // ИТОГО 11 тестов
    public void init(){
        game = Game.builder()
                .id(gameId)
                .title("Test Game")
                .build();

        user = User.builder()
                .id(gameId2)
                .email("asdasd@gmail.com")
                .build();

        reviewResponseDto = ReviewResponseDto.builder()
                .id(reviewId)
                .comment(comment)
                .userId(userId)
                .gameId(gameId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .atmosphereRate(atmosphereRate)
                .gameplayRate(gameplayRate)
                .graphicsRate(graphicsRate)
                .storyRate(storyRate)
                .build();

        reviewResponseDto2 = ReviewResponseDto.builder()
                .id(reviewId2)
                .comment(comment2)
                .userId(userId2)
                .gameId(gameId2)
                .createdAt(createdAt2)
                .updatedAt(updatedAt2)
                .atmosphereRate(atmosphereRate2)
                .gameplayRate(gameplayRate2)
                .graphicsRate(graphicsRate2)
                .storyRate(storyRate2)
                .build();

        reviewRequestDto = ReviewRequestDto.builder()
                .comment(comment)
                .userId(userId)
                .gameId(gameId)
                .atmosphereRate(atmosphereRate)
                .gameplayRate(gameplayRate)
                .graphicsRate(graphicsRate)
                .storyRate(storyRate)
                .build();

        reviewRequestDto2 = ReviewRequestDto.builder()
                .comment(comment2)
                .userId(userId2)
                .gameId(gameId2)
                .atmosphereRate(atmosphereRate2)
                .gameplayRate(gameplayRate2)
                .graphicsRate(graphicsRate2)
                .storyRate(storyRate2)
                .build();

        review = Review.builder()
                .id(reviewId)
                .comment(comment)
                .userId(userId)
                .gameId(gameId)
                .game(game)
                .user(user)
                .atmosphereRate(atmosphereRate)
                .gameplayRate(gameplayRate)
                .graphicsRate(graphicsRate)
                .storyRate(storyRate)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        review2 = Review.builder()
                .id(reviewId2)
                .comment(comment2)
                .userId(userId2)
                .gameId(gameId2)
                .atmosphereRate(atmosphereRate2)
                .gameplayRate(gameplayRate2)
                .graphicsRate(graphicsRate2)
                .storyRate(storyRate2)
                .createdAt(createdAt2)
                .updatedAt(updatedAt2)
                .build();
    }

    private void assertsThat(ReviewResponseDto savedReview) {
        assertThat(savedReview.getComment()).isEqualTo(comment);
        assertThat(savedReview.getAtmosphereRate()).isEqualTo(atmosphereRate);
        assertThat(savedReview.getGraphicsRate()).isEqualTo(graphicsRate);
        assertThat(savedReview.getGameplayRate()).isEqualTo(gameplayRate);
        assertThat(savedReview.getStoryRate()).isEqualTo(storyRate);
        assertThat(savedReview.getCreatedAt()).isEqualTo(createdAt);
        assertThat(savedReview.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(savedReview.getUserId()).isEqualTo(userId);
        assertThat(savedReview.getGameId()).isEqualTo(gameId);
    }

    @Test
    public void createReview_whenValidRequest_thenReturnsSavedReview(){
        when(reviewMapper.toEntity(reviewRequestDto)).thenReturn(review);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(new Game()));

        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(reviewResponseDto);

        ReviewResponseDto savedReview = reviewServiceLocal.createReview(reviewRequestDto);

        assertThat(savedReview).isNotNull();
        assertsThat(savedReview);

    }

    @Test
    public void getReviewById_whenExists_thenReturnsReview(){
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewResponseDto);

        ReviewResponseDto savedReview = reviewServiceLocal.getReviewById(reviewId);

        assertThat(savedReview).isNotNull();
        assertsThat(savedReview);
    }

    @Test
    public void getReviewById_whenNotFound_thenThrowNotFoundException(){
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> reviewServiceLocal.getReviewById(999L));

        assertThat(ex.getMessage()).contains("999");
    }

    @Test
    public void getReviews_whenListNotEmpty_thenReturnsReviews(){
        List<Review> reviews = List.of(review, review2);

        when(reviewRepository.findAll()).thenReturn(reviews);
        when(reviewMapper.toDto(any(Review.class))).thenAnswer(invocation -> {
            Review t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> reviewResponseDto;
                case 2 -> reviewResponseDto2;
                default -> throw new RuntimeException("Unknown review id: " + t.getId());
            };
        });

        List<ReviewResponseDto> result = reviewServiceLocal.getReviews();

        AssertionsForInterfaceTypes.assertThat(result).isNotNull();
        AssertionsForInterfaceTypes.assertThat(result).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(result).containsExactlyInAnyOrder(reviewResponseDto, reviewResponseDto2);
    }

    @Test
    public void getReviews_whenListIsEmpty_thenReturnsEmptyList(){
        when(reviewRepository.findAll()).thenReturn(List.of());

        List<ReviewResponseDto> result = reviewServiceLocal.getReviews();

        AssertionsForInterfaceTypes.assertThat(result).isEmpty();
    }

    @Test
    public void deleteReviewById_whenExists_thenReturnsDoesNotThrow(){
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewServiceLocal.deleteReviewById(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    public void deleteReviewById_whenNotFound_thenReturnsDoesNotThrow(){
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewServiceLocal.deleteReviewById(reviewId);

        verify(reviewRepository).deleteById(reviewId);
    }

    @Test
    public void updateReviewById_whenValidRequest_thenReturnsUpdatedReview(){
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        Mockito.lenient().doNothing().when(reviewMapper).updateReviewFromDto(any(ReviewRequestDto.class), any(Review.class));

        Mockito.lenient().when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewResponseDto);

        ReviewResponseDto savedReview = reviewServiceLocal.updateReviewById(reviewRequestDto, reviewId);

        assertThat(savedReview).isNotNull();
        assertsThat(savedReview);

        verify(reviewMapper).updateReviewFromDto(any(ReviewRequestDto.class), any(Review.class));
        verify(reviewMapper).toDto(any(Review.class));
    }

    @Test
    public void updateReviewById_whenNotFound_thenThrowNotFoundException(){
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> reviewServiceLocal.updateReviewById(reviewRequestDto, 999L));

        assertThat(ex.getMessage()).contains("999");
    }

    @Test
    public void updateReviewById_whenGameDoesNotExists_thenThrowInvalidIdException() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(User.builder()
                        .id(1L)
                        .build()));

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        InvalidIdException ex = assertThrows(InvalidIdException.class, () -> reviewServiceLocal.updateReviewById(reviewRequestDto, reviewId));

        assertThat(ex.getMessage()).contains("does not exist");

        verify(reviewRepository, never()).save(any());
    }

    @Test
    public void updateReviewById_whenUserDoesNotExists_thenThrowInvalidIdException() {
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        InvalidIdException ex = assertThrows(InvalidIdException.class, () -> reviewServiceLocal.updateReviewById(reviewRequestDto, reviewId));

        assertThat(ex.getMessage()).contains("does not exist");
                                                
        verify(reviewRepository, never()).save(any());
    }
}
