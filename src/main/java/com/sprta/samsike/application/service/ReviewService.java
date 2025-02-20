package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.restaurant.ReviewDTO;
import com.sprta.samsike.application.dto.restaurant.ReviewResponseDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.restaurant.Review;
import com.sprta.samsike.infrastructure.persistence.jpa.RestaurantRepository;
import com.sprta.samsike.infrastructure.persistence.jpa.ReviewRepository;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderService orderservice;
    private final RestaurantRepository restaurantRepository;

    public ReviewResponseDTO  createReview(UserDetailsImpl user, UUID orderid, ReviewDTO reviewDTO) {

        Member member = user.getMember();
        Order order = orderservice.getOrder(orderid);

        Review review = new Review();
        review.setMember(member);
        review.setOrder(order);
        review.setRestaurant(order.getRestaurant());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setUuid(review.getUuid());
        responseDTO.setRating(review.getRating());
        responseDTO.setComment(review.getComment());
        responseDTO.setCreatedAt(review.getCreatedAt());

        return responseDTO;
    }

    @Transactional
    public ReviewResponseDTO updateReview(UserDetailsImpl userDetails, UUID reviewId, ReviewDTO reviewDTO) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (!review.getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.REVW001,"작성자가 아닙니다.");
        }
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());

        ReviewResponseDTO responseDTO = new ReviewResponseDTO();
        responseDTO.setUuid(review.getUuid());
        responseDTO.setRating(review.getRating());
        responseDTO.setComment(review.getComment());
        responseDTO.setCreatedAt(review.getCreatedAt());

        return  responseDTO;
    }

    @Transactional
    public Object deleteReview(UserDetailsImpl userDetails, UUID reviewId) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (!review.getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.REVW001,"작성자가 아닙니다.");
        }

        review.setDeletedBy(member.getUsername());

        return "리뷰 삭제 완료";
    }

    public Object getReviewById(UUID restaruantid) {
        Restaurant restaurant = restaurantRepository.findByUuidAndDeletedAtIsNull(restaruantid).orElse(null);
        List<Review> reviews = reviewRepository.findAllByRestaurant(restaurant);

        List<ReviewResponseDTO> reviewDTOs = reviews.stream().map(review -> {
            ReviewResponseDTO dto = new ReviewResponseDTO();
            dto.setUuid(review.getUuid());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return reviewDTOs;
    }

    public Object getReviewRating(UUID restaruantid) {
        Restaurant restaurant = restaurantRepository.findById(restaruantid).orElse(null);
        List<Review> reviews = reviewRepository.findAllByRestaurant(restaurant);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        return String.format("%.1f", averageRating);
    }

    public Object searchReview(UserDetailsImpl userDetails, String comment){
        List<Review> reviews =  reviewRepository.findByCommentContainingIgnoreCaseAndDeletedAtIsNull(comment);
        List<ReviewResponseDTO> reviewDTOs = reviews.stream().map(review -> {
            ReviewResponseDTO dto = new ReviewResponseDTO();
            dto.setUuid(review.getUuid());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
        return reviewDTOs;
    }
}
