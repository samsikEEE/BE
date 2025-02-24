package com.sprta.samsike.infrastructure.security;

import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.persistence.jpa.MemberRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UerDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMB001,"일치하는 아이디 없음"));
        return new UserDetailsImpl(member);
    }
}
