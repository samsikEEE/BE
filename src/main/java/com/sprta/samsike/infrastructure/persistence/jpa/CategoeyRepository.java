package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.restaurant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoeyRepository extends JpaRepository<Category, UUID> {


}
