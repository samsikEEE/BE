package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.restaurant.ReviewDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.restaurant.Review;
import com.sprta.samsike.infrastructure.persistence.jpa.ReviewRepository;
import com.sprta.samsike.infrastructure.security.JwtUtil;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final Orderservice orderservice;

    public Review createReview(UserDetailsImpl user, UUID orderid, ReviewDTO reviewDTO) {

        Member member = user.getMember();
        Order order = orderservice.getOrder(orderid);

        Review review = new Review();
        review.setMember(member);
        review.setOrder(order);
        review.setRestaurant(order.getRestaurant());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public Review updateReview(UserDetailsImpl userDetails, UUID reviewId, ReviewDTO reviewDTO) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElse(null);
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        return  review;
    }

    @Transactional
    public Object deleteReview(UserDetailsImpl userDetails, UUID reviewId) {
        Member member = userDetails.getMember();
        Review review = reviewRepository.findById(reviewId).orElse(null);

        if (!review.getMember().equals(member)) {
            throw new CustomException(ErrorCode.REVW001,"작성자가 아닙니다.");
        }

        reviewRepository.delete(review);

        return "리뷰 삭제 완료";
    }

    public Object getReview(UUID reviewid) {
        Review review = reviewRepository.findById(reviewid).orElse(null);
        return review;
    }
}
