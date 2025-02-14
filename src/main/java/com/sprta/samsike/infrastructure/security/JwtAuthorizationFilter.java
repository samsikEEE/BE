package com.sprta.samsike.infrastructure.security;


import com.sprta.samsike.domain.member.Tokens;
import com.sprta.samsike.infrastructure.persistence.jpa.TokensRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
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
