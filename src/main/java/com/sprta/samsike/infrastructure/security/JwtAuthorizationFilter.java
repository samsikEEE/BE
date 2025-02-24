package com.sprta.samsike.infrastructure.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.domain.member.Tokens;
import com.sprta.samsike.domain.persistence.jpa.TokensRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import com.sprta.samsike.presentation.advice.ErrorData;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT생성")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokensRepository tokensRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
        String tokenValue = jwtUtil.getJwtFromToken(request);

        if (StringUtils.hasText(tokenValue)) {
            // 먼저 토큰이 블랙리스트에 등록되어 있는지 확인
            Optional<Tokens> tokenEntityOpt = tokensRepository.findByTokenValue(JwtUtil.BEARER_PREFIX +tokenValue);
            if (tokenEntityOpt.isPresent() && tokenEntityOpt.get().isBlacklisted()) {
                log.error("Token is blacklisted");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                throw new CustomException(ErrorCode.AUTH001,"로그아웃된 토큰");
            }
            if (StringUtils.hasText(tokenValue)) {
                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token not valid");
                    return;
                }

                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }

        }
        filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // 필터 내에서 발생한 CustomException을 잡아서 JSON 형태로 응답 작성
            response.setContentType("application/json;charset=UTF-8");
            // 적절한 HTTP 상태 코드를 설정 (예: 인증 실패이므로 401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 에러 응답 객체 생성 (ApiResponse, ErrorData는 기존에 사용하던 클래스)
            ErrorData errorData = new ErrorData(
                    e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails()
            );
            ApiResponseDTO<ErrorData> errorResponse = new ApiResponseDTO<>("fail", errorData);

            // ObjectMapper를 사용해 JSON 문자열로 변환
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(errorResponse);

            // 응답 작성 후 종료
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            return;
        }
    }

    public void setAuthentication(String username) {
        Authentication authentication = createAutntication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAutntication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
