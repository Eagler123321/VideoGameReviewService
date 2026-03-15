package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.Review;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.ReviewMapper;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.repository.ReviewRepository;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.service.noImp.ReviewService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReviewServiceLocal implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceLocal(ReviewMapper reviewMapper, ReviewRepository reviewRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        Review review = reviewRepository.save(reviewMapper.toEntity(reviewRequestDto));

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        log.info("User with Email {} left a review with title {}",
                userRepository.getReferenceById(reviewRequestDto.getUserId()).getEmail(),
                gameRepository.getReferenceById(reviewRequestDto.getGameId()).getTitle());

        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReviewById(ReviewRequestDto reviewRequestDto, Long id) {
        log.debug("Review is being updated with id {}", id);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id " + id));

        reviewMapper.updateReviewFromDto(reviewRequestDto, review);

        review.setUpdatedAt(LocalDateTime.now());

        log.info("Review was updated with user email {} and title {} and id {}",
                userRepository.getReferenceById(reviewRequestDto.getUserId()).getEmail(),
                gameRepository.getReferenceById(reviewRequestDto.getGameId()).getTitle(),
                id);

        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id " + id));

        log.debug("A review with userId {} and gameId {} is received",
                review.getUser(),
                review.getGame().getId());

        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public List<ReviewResponseDto> getReviews() {
        log.debug("Receiving all reviews...");

        List<Review> reviews = reviewRepository.findAll();

        log.info("All reviews is received! Count {}", reviews.size());

        return reviews.stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReviewById(Long id) {
        String email = reviewRepository.findById(id)
                .map(Review::getUser)
                .map(User::getEmail)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        String title = reviewRepository.findById(id)
                .map(Review::getGame)
                .map(Game::getTitle)
                .orElseThrow(() -> new NotFoundException("Game not found with id " + id));

        log.debug("A review is being deleted with title {}", title);

        reviewRepository.deleteById(id);

        log.info("A review with user email {} and game title {} was deleted", email, title);
    }
}
