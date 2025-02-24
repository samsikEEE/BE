package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByCategory(String category);

    List<Category> findAllByDeletedAtIsNull();

    List<Category> findAllByCategoryContainingAndDeletedAtIsNull(String categoryName);
}
