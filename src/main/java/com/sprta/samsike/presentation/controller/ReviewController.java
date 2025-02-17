package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.restaurant.ReviewDTO;
import com.sprta.samsike.application.service.ReviewService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{reviewid}")
    public ResponseEntity<ApiResponseDTO> getReview(@PathVariable UUID reviewid) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success",reviewService.getReview(reviewid)));
    }

    @PostMapping("/{orderid}")
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable("orderid") UUID orderId,
            @RequestBody ReviewDTO reviewDTO
            ) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", reviewService.createReview(user,orderId,reviewDTO)));
    }

    @PutMapping("/{reviewid}")
    public ResponseEntity<?> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable("reviewid") UUID reviewId,
                                          @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success",reviewService.updateReview(userDetails, reviewId,reviewDTO)));

    }

    @DeleteMapping("/{reviewid}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @PathVariable("reviewid") UUID reviewId){
        return ResponseEntity.ok(new ApiResponseDTO<>("success",reviewService.deleteReview(userDetails, reviewId)));
    }



}
