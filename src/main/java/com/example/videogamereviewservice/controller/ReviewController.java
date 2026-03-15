package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.service.local.ReviewServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewController {
    private final ReviewServiceLocal reviewServiceLocal;

    public ReviewController(ReviewServiceLocal reviewServiceLocal) {
        this.reviewServiceLocal = reviewServiceLocal;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewServiceLocal.createReview(reviewRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> updateReviewById(@Valid @RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(reviewServiceLocal.updateReviewById(reviewRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(reviewServiceLocal.getReviewById(id));
    }
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAllReviews(){
        return ResponseEntity.status(HttpStatus.OK).body(reviewServiceLocal.getReviews());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewById(@PathVariable Long id){
        reviewServiceLocal.deleteReviewById(id);
        return ResponseEntity.noContent().build();
    }
}
