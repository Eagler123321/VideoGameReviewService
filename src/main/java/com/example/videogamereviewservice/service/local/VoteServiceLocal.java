package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.VoteRequestDto;
import com.example.videogamereviewservice.dto.response.VoteResponseDto;
import com.example.videogamereviewservice.entity.Vote;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.VoteMapper;
import com.example.videogamereviewservice.repository.ReviewRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.repository.VoteRepository;
import com.example.videogamereviewservice.service.noImp.VoteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.videogamereviewservice.service.validation.ValidationChecker.checkInvalidId;

@Service
@Slf4j
public class VoteServiceLocal implements VoteService {
    private final VoteMapper voteMapper;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public VoteServiceLocal(VoteMapper voteMapper, VoteRepository voteRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.voteMapper = voteMapper;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public VoteResponseDto createVote(VoteRequestDto voteRequestDto) {
        checkInvalidId(userRepository, voteRequestDto.getUserId(), "User");
        checkInvalidId(reviewRepository, voteRequestDto.getReviewId(), "Review");

        Vote vote = voteRepository.save(voteMapper.toEntity(voteRequestDto));

        vote.setCreatedAt(LocalDateTime.now());

        log.info("{} was given with userId {} and reviewId {}", voteRequestDto.getVoteType(), voteRequestDto.getUserId(), voteRequestDto.getReviewId());

        return voteMapper.toDto(vote);
    }

    @Override
    @Transactional
    public VoteResponseDto updateVoteById(VoteRequestDto voteRequestDto, Long id) {
        log.debug("{} is being changed with userId {} and reviewId {}", voteRequestDto.getVoteType(), voteRequestDto.getUserId(), voteRequestDto.getReviewId());

        Vote receivedVote = voteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vote is not found with id " + id));

        voteMapper.updateVoteFromDto(voteRequestDto, receivedVote);

        log.info("{} is changed with userId {} and reviewId {}", voteRequestDto.getVoteType(), voteRequestDto.getUserId(), voteRequestDto.getReviewId());

        return voteMapper.toDto(receivedVote);
    }

    @Override
    @Transactional
    public VoteResponseDto getVoteById(Long id) {
        Vote receivedVote = voteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vote is not found with id " + id));

        log.debug("{} was found with id {}", receivedVote.getVoteType(), receivedVote.getId());

        return voteMapper.toDto(receivedVote);
    }

    @Override
    @Transactional
    public List<VoteResponseDto> getVotes() {
        log.debug("Receiving all votes...");

        List<Vote> receivedVotes = voteRepository.findAll();
        
        log.info("All votes was received! Count {}", receivedVotes.size());

        return receivedVotes.stream()
                .map(voteMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteVoteById(Long id) {
        Vote receivedVote = voteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vote is not found with id " + id));

        log.debug("{} is being deleted with id {}", receivedVote.getVoteType(), receivedVote.getId());

        voteRepository.deleteById(id);

        log.info("{} is deleted with id {}", receivedVote.getVoteType(), receivedVote.getId());
    }
}
