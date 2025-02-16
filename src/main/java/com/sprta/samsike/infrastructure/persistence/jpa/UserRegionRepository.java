package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.region.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRegionRepository extends JpaRepository<UserRegion, UUID> {

}
