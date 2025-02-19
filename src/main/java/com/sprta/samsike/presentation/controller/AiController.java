package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.service.AiService;
import com.sprta.samsike.application.dto.ai.AiRequestDto;
import com.sprta.samsike.application.dto.ai.AiResponseDto;
import com.sprta.samsike.application.dto.ai.AiLogDto;
import com.sprta.samsike.domain.member.Member;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PostMapping("/request")
    public AiResponseDto processAiRequest(
            @RequestBody AiRequestDto aiRequestDto,
            @RequestAttribute Member member // 인증된 사용자 정보
    )
    {
        String modelVersion = "gpt-4"; // AI 모델 버전을 서비스로 전달
        return aiService.processAiRequest(aiRequestDto, member, modelVersion);
    }

    /**
     * 단일 AI 로그 조회
     * @param uuid 요청한 AI 로그의 UUID
     * @return AiLogDto
     */
    @GetMapping("/logs/{uuid}")
    public AiLogDto getAiLog(
            @PathVariable UUID uuid,
            @RequestAttribute Member member // 인증된 사용자 정보
    )
    {
        return aiService.getAiLog(uuid, member);
    }

    /**
     * 전체 AI 로그 조회
     * @return List<AiLogDto>
     */
    @GetMapping("/logs")
    public List<AiLogDto> getAllLogs(@RequestAttribute Member member) {
        return aiService.getAllLogs(member);
    }

}
