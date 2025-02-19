package com.sprta.samsike.infrastructure.persistence.jpa;

import com.sprta.samsike.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsernameAndDeletedAtIsNull(String username);

    Page<Member> findByDeletedAtIsNull(Pageable pageable);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndRoleAndDeletedAtIsNull(String userName, String string);

    Optional<Member> findByUsernameContainingIgnoreCaseAndDeletedAtIsNull(String string);
}
