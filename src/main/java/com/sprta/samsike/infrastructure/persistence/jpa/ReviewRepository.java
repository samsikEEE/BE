package com.sprta.samsike.infrastructure.persistence.jpa;

import com.querydsl.core.Tuple;
import com.sprta.samsike.application.dto.restaurant.ReviewCountDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.restaurant.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByMember(Member member);

    List<Review> findAllByRestaurant(Restaurant restaurant);


    @Query("SELECT new com.sprta.samsike.application.dto.restaurant.ReviewCountDto(COUNT(r), AVG(r.rating)) FROM Review r WHERE r.restaurant.uuid = :restaurantId")
    ReviewCountDto findCountAndAvg(@Param("restaurantId") UUID restaurantId);
}
