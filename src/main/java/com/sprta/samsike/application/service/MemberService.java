package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.member.LoginDTO;
import com.sprta.samsike.application.dto.member.ProfileDTO;
import com.sprta.samsike.application.dto.member.SignupRequestDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.restaurant.ReviewResponseDTO;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.member.Tokens;
import com.sprta.samsike.domain.restaurant.Review;
import com.sprta.samsike.infrastructure.persistence.jpa.MemberRepository;
import com.sprta.samsike.infrastructure.persistence.jpa.ReviewRepository;
import com.sprta.samsike.infrastructure.persistence.jpa.TokensRepository;
import com.sprta.samsike.infrastructure.security.JwtUtil;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "로그인 및 회원가입 기능")
@RequiredArgsConstructor
@Service
public class MemberService {

    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
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
            Optional<Tokens> tokenOpt = tokensRepository.findByUser(principal.getMember());
            Tokens tokenEntity;
            if (tokenOpt.isPresent()) {
                // 기존 토큰이 있다면, 새 토큰 값으로 업데이트
                tokenEntity = tokenOpt.get();
                String newAccessToken = jwtUtil.createToken(principal.getUsername(), role);
                String newRefreshToken = jwtUtil.createRefreshToken(principal.getUsername(), role);
                tokenEntity.setTokenValue(newAccessToken);
                tokenEntity.setRefreshToken(newRefreshToken);

                // 현재 시간을 기준으로 만료 시간 재계산
                Date now = new Date();
                LocalDateTime accessExp = LocalDateTime.ofInstant(
                        new Date(now.getTime() + jwtUtil.getTokenTime()).toInstant(),
                        ZoneId.systemDefault());
                LocalDateTime refreshExp = LocalDateTime.ofInstant(
                        new Date(now.getTime() + jwtUtil.getRefreshTokenTime()).toInstant(),
                        ZoneId.systemDefault());
                tokenEntity.setAccessTokenExpiration(accessExp);
                tokenEntity.setRefreshTokenExpiration(refreshExp);

                tokensRepository.save(tokenEntity);
            } else {
                // 기존 토큰이 없다면, 새 토큰 엔티티 생성 후 저장
                tokenEntity = jwtUtil.createTokenEntity(principal.getUsername(), role, principal.getMember());
                tokensRepository.save(tokenEntity);
            }

            // 4. 토큰과 메시지를 응답 데이터에 포함
            return new ApiResponseDTO<>("success", Map.of(
                    "accessToken", tokenEntity.getTokenValue(),
                    "refreshToken", tokenEntity.getRefreshToken(),
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
        String name = requestDto.getName();
        String email = requestDto.getEmail();

        Optional<Member> checkUsername = memberRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        String roleStr = (requestDto.getRole() == null || requestDto.getRole().isBlank())
                ? "ROLE_CUSTOMER"
                : requestDto.getRole().toUpperCase();

        // 요청이 "ROLE_CUSTOMER", "ROLE_OWNER", "ROLE_MANAGER", "ROLE_MASTER" 인지 검증
        MemberRoleEnum roleEnum = MemberRoleEnum.valueOf(roleStr);

        // 사용자 등록
        Member member = new Member(username, password,name, email, roleEnum);
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
    public String deleteMember(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        // 소프트 삭제 처리
        member.softDelete();
        member.setDeletedBy(member.getUsername());
        // 상태 변경(및 삭제 필드 업데이트)를 반영하기 위해 save 호출
        memberRepository.save(member);

        return "회원 탈퇴 완료";
    }

    public ApiResponseDTO<?> logout(HttpServletRequest request) {
        String token = "Bearer "+ jwtUtil.getJwtFromToken(request);

        if (!StringUtils.hasText(token)) {
            return new ApiResponseDTO<>("fail", "토큰이 존재하지 않습니다.");
        }

        Optional<Tokens> tokenEntityOpt = tokensRepository.findByTokenValue(token);

        if (tokenEntityOpt.isPresent()) {
            Tokens tokenEntity = tokenEntityOpt.get();
            // 블랙리스트 처리)
            tokenEntity.blacklist();
            tokensRepository.save(tokenEntity);
            return new ApiResponseDTO<>("success", "로그아웃 성공");
        } else {
            return new ApiResponseDTO<>("fail", "토큰 정보가 존재하지 않습니다.");
        }
    }

    public Optional getMemberProfile(UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        ProfileDTO profile = new ProfileDTO(member.getUsername(), member.getName(), member.getEmail(),member.getCreatedAt());
        return Optional.of(profile);
    }

    public List<Member> getAllMemberProfile(UserDetailsImpl userDetails){
        Member member = userDetails.getMember();

        boolean hasManagerOrAbove = userDetails.getAuthorities().stream()
                .anyMatch(auth->auth.getAuthority().equals("ROLE_MANAGER") || auth.getAuthority().equals("ROLE_MANAGER"));

        if(!hasManagerOrAbove){
            throw new CustomException(ErrorCode.AUTH001,"권한이 없습니다.");
        }

        return memberRepository.findAll();
    }

    public Object refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.getJwtFromToken(request);

        if (!StringUtils.hasText(refreshToken)) {
            return new ApiResponseDTO<>("fail", "Refresh Token이 존재하지 않습니다.");
        }

        // Refresh Token 검증
        Optional<Tokens> tokenEntityOpt = tokensRepository.findByRefreshToken(JwtUtil.BEARER_PREFIX + refreshToken);
        if (tokenEntityOpt.isEmpty()) {
            return new ApiResponseDTO<>("fail", "유효하지 않은 Refresh Token입니다.");
        }

        Tokens tokenEntity = tokenEntityOpt.get();
        if (tokenEntity.isBlacklisted()) {
            return new ApiResponseDTO<>("fail", "로그아웃된 토큰입니다.");
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            return new ApiResponseDTO<>("fail", "만료된 Refresh Token입니다.");
        }

        // Refresh Token 정보에서 사용자 가져오기
        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String username = claims.getSubject();
        MemberRoleEnum role = MemberRoleEnum.valueOf(claims.get(JwtUtil.AUTHORIZATION_KEY).toString());

        // 새로운 Access Token 생성
        String newAccessToken = jwtUtil.createToken(username, role);
        tokenEntity.setTokenValue(newAccessToken);
        tokensRepository.save(tokenEntity);

        return Map.of(
                "accessToken", newAccessToken,
                "message", "Access Token이 갱신되었습니다."
        );
    }

    @Transactional
    public Object modifyMemberProfile(UserDetailsImpl userDetails, ProfileDTO profileDTO) {
        Member member = userDetails.getMember();
        member.setName(profileDTO.getName());
        member.setEmail(profileDTO.getEmail());
        return "프로필 수정 완료";
    }

    public Object getReviews(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        List<Review> reviews =  reviewRepository.findAllByMember(member);

        List<ReviewResponseDTO> reviewDTOs = reviews.stream().map(review -> {
            ReviewResponseDTO dto = new ReviewResponseDTO();
            dto.setUuid(review.getUuid());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());

        return reviewDTOs;
    }
}
