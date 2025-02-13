package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.member.LoginDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.infrastructure.persistence.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public ApiResponseDTO<?> login(LoginDTO loginDTO) {

        memberRepository.findByUsername(loginDTO.getUsername());
        return null;
    }
}
