package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.region.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRegionRepository extends JpaRepository<UserRegion, UUID> {
    Optional<UserRegion> findByMemberAndIsDefaultTrue(Member member);
}
