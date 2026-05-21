package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.base.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.entity.Review;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.repository.ReviewRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.service.local.VoteServiceLocal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
class VoteServiceLocalIT {
    @Autowired
    private VoteServiceLocal voteServiceLocal;

    private User testUser;
    private Review testReview;
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();
        testUser = userRepository.save(testUser);

        testReview = Review.builder()
                .comment("Test review")
                .user(testUser)
                .userId(testUser.getId())
                .build();
        testReview = reviewRepository.save(testReview);
    }

    private static VoteRequestDto createVoteRequestDto(String name) {
        return VoteRequestDto.builder()
                .voteType(name)
                .build();
    }

    @Test
    void createVote_whenValidData_thenReturnsSavedVote() {
        VoteRequestDto requestDto = createVoteRequestDto("LIKE");

        VoteResponseDto savedVote = voteServiceLocal.createVote(requestDto);

        assertThat(savedVote).isNotNull();
        assertThat(savedVote.getId()).isNotNull();
        assertThat(savedVote.getVoteType()).isEqualTo("LIKE");
        assertThat(savedVote.getUserId()).isEqualTo(testUser.getId());
        assertThat(savedVote.getReviewId()).isEqualTo(testReview.getId());
    }

    @Test
    void getVoteById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> voteServiceLocal.getVoteById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
