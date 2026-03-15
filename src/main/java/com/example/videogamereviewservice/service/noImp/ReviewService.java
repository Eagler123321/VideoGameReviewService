package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto);

    ReviewResponseDto updateReviewById(ReviewRequestDto reviewRequestDto, Long id);

    ReviewResponseDto getReviewById(Long id);

    List<ReviewResponseDto> getReviews();

    void deleteReviewById(Long id);
}
