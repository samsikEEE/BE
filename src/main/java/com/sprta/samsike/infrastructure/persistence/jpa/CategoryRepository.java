package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByCategory(String category);

}
