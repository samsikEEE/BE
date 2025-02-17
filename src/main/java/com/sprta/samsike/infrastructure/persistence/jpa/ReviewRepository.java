package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.domain.restaurant.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByMember(Member member);

    List<Review> findAllByRestaurant(Restaurant restaurant);
}
