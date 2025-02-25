package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.service.AiService;
import com.sprta.samsike.application.dto.ai.AiRequestDto;
import com.sprta.samsike.application.dto.ai.AiResponseDto;
import com.sprta.samsike.application.dto.ai.AiLogDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * AI 요청 처리 및 결과 반환
     */
    @Operation(summary = "Ai 질문 요청", description = "Ai에게 질문을 하 추천을 받습니다.")
    @PostMapping("/request")
    public AiResponseDto processAiRequest(
            @RequestBody AiRequestDto aiRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보
    )
    {
        // UserDetailsImpl에서 Member 객체 가져오기
        Member member = userDetails.getMember();
        String modelVersion = "gemini-1.5-flash"; // AI 모델 버전을 서비스로 전달
        return aiService.processAiRequest(aiRequestDto, member, modelVersion);
    }

    /**
     * 단일 AI 로그 조회
     * @param uuid 요청한 AI 로그의 UUID
     * @return AiLogDto
     */
    @Operation(summary = "특정 로그 조회", description = "특정 로그의 내용을 조회합니다")
    @GetMapping("/logs/{uuid}")
    public AiLogDto getAiLog(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보
    )
    {
        Member member = userDetails.getMember();
        return aiService.getAiLog(uuid, member);
    }

    /**
     * ✅ AI 로그 조회 (정렬 + 페이지네이션)
     */
    @Operation(summary = "로그 정렬 조회", description = "요청과 응답의 로그를 정렬 조회합니다")
    @GetMapping("/logs")
    public ResponseEntity<Page<AiLogDto>> getAiLogs(
            @AuthenticationPrincipal UserDetailsImpl userDetails,  // ✅ 인증된 사용자 정보
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "ascending", defaultValue = "true") boolean ascending,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        // ✅ UserDetailsImpl에서 Member 객체 가져오기
        Member member = userDetails.getMember();

        // AI 로그 조회 서비스 호출
        Page<AiLogDto> logs = aiService.getAllLogs(member, sortBy, ascending, page, size);
        return ResponseEntity.ok(logs);
    }

}
