package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.member.LoginDTO;
import com.sprta.samsike.application.dto.member.SignupRequestDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.MemberService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "로그인 및 회원가입")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(memberService.login(loginDTO));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success",memberService.logout(request)));

    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("fail", "로그인 실패"));
        }

        memberService.signup(requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "회원가입 성공"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getMemberProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.getMemberProfile(userDetails)));
    }


}
