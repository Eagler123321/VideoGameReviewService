package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.request.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.Review;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.entity.Vote;
import com.example.videogamereviewservice.mapper.VoteMapper;
import com.example.videogamereviewservice.repository.ReviewRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.repository.VoteRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {
    @Mock
    private VoteMapper voteMapper;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private VoteServiceLocal voteServiceLocal;

    private Vote vote;
    private Vote vote2;
    private final Long voteId = 1L;
    private final Long voteId2 = 2L;
    private final Long userId = 3L;
    private final Long userId2 = 4L;
    private final Long reviewId = 5L;
    private final Long reviewId2 = 6L;
    private final String voteType = "Like";
    private final String voteType2 = "Dislike";
    private final LocalDateTime createdAt = LocalDateTime.of(2025, 5, 23, 15, 24,47);
    private final LocalDateTime createdAt2 = LocalDateTime.of(2023, 1, 13, 22, 4,12);
    private VoteRequestDto voteRequestDto;
    private VoteRequestDto voteRequestDto2;
    private VoteResponseDto voteResponseDto;
    private VoteResponseDto voteResponseDto2;


    @BeforeEach
    public void init(){
        voteRequestDto = VoteRequestDto.builder()
                .voteType(voteType)
                .reviewId(reviewId)
                .userId(userId)
                .build();
        voteRequestDto2 = VoteRequestDto.builder()
                .voteType(voteType2)
                .reviewId(reviewId2)
                .userId(userId2)
                .build();
        voteResponseDto = VoteResponseDto.builder()
                .id(voteId)
                .createdAt(createdAt)
                .voteType(voteType)
                .reviewId(reviewId)
                .userId(userId)
                .build();
        voteResponseDto2 = VoteResponseDto.builder()
                .id(voteId2)
                .createdAt(createdAt2)
                .voteType(voteType2)
                .reviewId(reviewId2)
                .userId(userId2)
                .build();

        vote = Vote.builder()
                .id(voteId)
                .createdAt(createdAt)
                .voteType(voteType)
                .reviewId(reviewId)
                .userId(userId)
                .build();
        vote2 = Vote.builder()
                .id(voteId2)
                .createdAt(createdAt2)
                .voteType(voteType2)
                .reviewId(reviewId2)
                .userId(userId2)
                .build();
    }

    private void assertsThat(VoteResponseDto savedVote) {
        assertThat(savedVote.getVoteType()).isEqualTo(voteType);
        assertThat(savedVote.getReviewId()).isEqualTo(reviewId);
        assertThat(savedVote.getUserId()).isEqualTo(userId);
        assertThat(savedVote.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    public void createVote_whenValidRequest_thenReturnsSavedReview(){
        when(voteMapper.toEntity(voteRequestDto)).thenReturn(vote);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(new Review()));

        when(voteRepository.save(any(Vote.class))).thenReturn(vote);
        when(voteMapper.toDto(vote)).thenReturn(voteResponseDto);

        VoteResponseDto savedVote = voteServiceLocal.createVote(voteRequestDto);

        assertThat(savedVote).isNotNull();
        assertsThat(savedVote);

    }

    @Test
    public void getVoteById_whenExists_thenReturnsVote(){
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));
        when(voteMapper.toDto(any(Vote.class))).thenReturn(voteResponseDto);

        VoteResponseDto savedVote = voteServiceLocal.getVoteById(voteId);

        assertThat(savedVote).isNotNull();
        assertsThat(savedVote);
    }

    @Test
    public void getVotes_whenListNotEmpty_thenReturnsVotes(){
        List<Vote> votes = List.of(vote, vote2);

        when(voteRepository.findAll()).thenReturn(votes);
        when(voteMapper.toDto(any(Vote.class))).thenAnswer(invocation -> {
            Vote t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> voteResponseDto;
                case 2 -> voteResponseDto2;
                default -> throw new RuntimeException("Unknown vote id: " + t.getId());
            };
        });

        List<VoteResponseDto> result = voteServiceLocal.getVotes();

        AssertionsForInterfaceTypes.assertThat(result).isNotNull();
        AssertionsForInterfaceTypes.assertThat(result).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(result).containsExactlyInAnyOrder(voteResponseDto, voteResponseDto2);
    }

    @Test
    public void deleteVoteById_whenExists_thenReturnsDoesNotThrow(){
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));

        voteServiceLocal.deleteVoteById(voteId);

        verify(voteRepository).deleteById(voteId);
    }

    @Test
    public void updateVoteById_whenValidRequest_thenReturnsUpdatedVote(){
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(vote));

        Mockito.lenient().doNothing().when(voteMapper).updateVoteFromDto(any(VoteRequestDto.class), any(Vote.class));

        Mockito.lenient().when(voteMapper.toDto(any(Vote.class))).thenReturn(voteResponseDto);

        VoteResponseDto savedVote = voteServiceLocal.updateVoteById(voteRequestDto, voteId);

        assertThat(savedVote).isNotNull();
        assertsThat(savedVote);

        verify(voteMapper).updateVoteFromDto(any(VoteRequestDto.class), any(Vote.class));
        verify(voteMapper).toDto(any(Vote.class));
    }
}