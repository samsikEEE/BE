package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.member.LoginDTO;
import com.sprta.samsike.application.dto.member.ProfileDTO;
import com.sprta.samsike.application.dto.member.SignupRequestDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.MemberService;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.logout(request)));

    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String message = fieldError.getDefaultMessage();
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                return ResponseEntity.ok(new ApiResponseDTO<>("fail", message));
            }
        }

        memberService.signup(requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("success", "회원가입 성공"));
    }

    @GetMapping("/resign")
    public ResponseEntity<?> resign(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success",memberService.deleteMember(userDetails)));
    }

    @PostMapping("/refresh")
    @Operation(summary = "리프레시 토큰으로 access 토큰 갱신")
    public ResponseEntity<ApiResponseDTO<?>> refreshAccessToken(HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.refreshAccessToken(request)));
    }

    @GetMapping("/")
    @Operation(summary = "회원 정보 조회")
    public ResponseEntity<?> getMemberProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.getMemberProfile(userDetails)));
    }

    @PostMapping("/")
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<?> modifyMemberProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.modifyMemberProfile(userDetails, profileDTO)));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 회원을 조회",description = "MASTER만 가능")
    public Page<Member> getPagedAndSortedMembers(
            UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {
        return memberService.getPagedAndSortedMembers(page-1, size, sortBy, ascending,userDetails);
    }

    @GetMapping("/search/{string}")
    @Operation(summary = "회원 검색",description = "MASTER ONLY")
    public ResponseEntity searchMembers(@PathVariable("string") String string, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success",memberService.searchMemberByUsername(string, userDetails)));
    }

    @GetMapping("/reviews")
    @Operation(summary = "본인이 작성한 모든 리뷰 확인")
    public ResponseEntity<?> getReviews(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponseDTO<>("success", memberService.getReviews(userDetails)));
    }


}
