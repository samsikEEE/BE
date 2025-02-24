package com.sprta.samsike.domain.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.region.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRegionRepository extends JpaRepository<UserRegion, UUID> {
    Optional<UserRegion> findByMemberAndIsDefaultTrueAndDeletedAtIsNull(Member member);

}
