package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.application.dto.restaurant.ReviewCountDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.restaurant.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByMember(Member member);

    List<Review> findAllByRestaurantAndDeletedAtIsNull(Restaurant restaurant);

    Page<Review> findByCommentContainingIgnoreCaseAndDeletedAtIsNull(Pageable pageable, String comment);

    @Query("SELECT new com.sprta.samsike.application.dto.restaurant.ReviewCountDto(COUNT(r), AVG(r.rating)) FROM Review r WHERE r.restaurant.uuid = :restaurantId and r.deletedAt is null")
    ReviewCountDto findCountAndAvg(@Param("restaurantId") UUID restaurantId);
}
