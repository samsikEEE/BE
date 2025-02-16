package com.sprta.samsike.infrastructure.persistence.jpa;


import com.sprta.samsike.domain.region.SggCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SggCodeRepository extends JpaRepository<SggCode, UUID> {

    Optional<SggCode> findBySggCd(String sggCode);
}
