package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.member.LoginDTO;
import com.sprta.samsike.application.dto.member.SignupRequestDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.infrastructure.persistence.jpa.MemberRepository;
import com.sprta.samsike.infrastructure.security.JwtUtil;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j(topic = "로그인 및 회원가입 기능")
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public ApiResponseDTO<?> login(LoginDTO loginDTO) {
        try {
            // 1. AuthenticationManager를 통해 인증 처리 (내부에서 아이디/비밀번호 검증)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            // 2. 인증 성공 시, UserDetails를 통해 사용자 정보 획득
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
            MemberRoleEnum role = MemberRoleEnum.valueOf(principal.getMember().getRole());

            // 3. JWT 토큰 생성
            String accessToken = jwtUtil.createToken(principal.getUsername(), role);

            // 4. 토큰과 메시지를 응답 데이터에 포함
            // JwtAuthenticationFilter에서 /api/member/login 요청을 이미 처리하고 있다면, 이 메서드(그리고 컨트롤러)를 호출하지 않음
            return new ApiResponseDTO<>("success", Map.of(
                    "accessToken", accessToken,
                    "message", "로그인 성공"
            ));
        } catch (Exception e) {
            log.info(e.getMessage());
            log.error("Authentication failed", e);
            throw new CustomException(ErrorCode.MEMB002, "로그인 정보 불일치");
        }
    }

    public void signup(@Valid SignupRequestDTO requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkUsername = memberRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        String name = requestDto.getName();

        // 사용자 등록
        Member member = new Member(username, password,name, email);
        // 회원가입시에는 context에 사용자정보가 없어서 수동 등록
        // 임시 인증 객체 생성 및 SecurityContext 설정
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        memberRepository.save(member);
        // SecurityContext를 초기화
        SecurityContextHolder.clearContext();

    }

    @Transactional
    public void deleteMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        // 소프트 삭제 처리
        member.softDelete();
        // 상태 변경(및 삭제 필드 업데이트)를 반영하기 위해 save 호출
        memberRepository.save(member);
    }
}
