package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndDeletedAtIsNull(String username);

    List<Member> findByDeletedAtIsNull();

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndRoleAndDeletedAtIsNull(String userName, String string);
}
