package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
}
