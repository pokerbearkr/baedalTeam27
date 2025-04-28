package org.example.baedalteam27.domain.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.review.dto.request.ReviseReviewRequest;
import org.example.baedalteam27.domain.review.dto.request.WriteReviewRequest;
import org.example.baedalteam27.domain.review.dto.response.ReviewResponse;
import org.example.baedalteam27.domain.review.service.ReviewService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/order/{orderId}/reviews")
    public ResponseEntity<Void> write(@PathVariable Long orderId,
                                      @RequestBody WriteReviewRequest dto){

        reviewService.write(orderId, dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/review")
    public ResponseEntity<Void> revise(@LoginUser Long userId, @RequestBody ReviseReviewRequest dto){
        reviewService.revise(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> delete(@LoginUser Long userId, @PathVariable Long reviewId){
        reviewService.delete(userId, reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/store/{storeId}/review")
    public ResponseEntity<Page<ReviewResponse>> getAllReviewsAboutStore(@PathVariable Long storeId, Pageable pageable) {

        return ResponseEntity.ok(reviewService.getAllReviewsAboutStore(storeId, pageable));
    }
}
