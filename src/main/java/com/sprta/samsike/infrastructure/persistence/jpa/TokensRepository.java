package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
    Optional<Tokens> findByTokenValue(String token);

    Optional<Tokens> findByUser(Member user);

}
