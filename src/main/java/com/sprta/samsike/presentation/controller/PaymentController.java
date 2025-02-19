package com.sprta.samsike.presentation.controller;

import com.sprta.samsike.application.dto.payment.PaymentDetailsResponseDto;
import com.sprta.samsike.application.dto.payment.PaymentRequestDto;
import com.sprta.samsike.application.dto.payment.PaymentResponseDto;
import com.sprta.samsike.application.dto.request.RequestListDTO;
import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.service.PaymentService;
import com.sprta.samsike.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_OWNER')")
    @Operation(summary = "결제 내역 전체 조회", description = "사용자의 결제 목록을 조회합니다.")
    public ResponseEntity<ApiResponseDTO<Page<PaymentResponseDto>>> getPayments(
            @ParameterObject @ModelAttribute RequestListDTO requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<PaymentResponseDto> payments = paymentService.getPayment(userDetails.getMember(), requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", payments));
    }


    @GetMapping("/restaurants/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MASTER')")
    @Operation(summary = "관리자 결제 조회", description = "해당 가게의 결제 목록을 조회합니다.")
    public ResponseEntity<ApiResponseDTO<Page<PaymentResponseDto>>> getRestaurantPayments(
            @PathVariable("restaurantId") UUID restaurantId,
            @ParameterObject @ModelAttribute RequestListDTO requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<PaymentResponseDto> payments = paymentService.getRestaurantPayments(userDetails.getMember(), restaurantId,requestDto);

        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", payments));
    }


    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_OWNER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_MASTER')")
    @Operation(summary = "상세 결제 내역 조회", description = "특정 결제의 상세정보를 조회합니다.")
    public ResponseEntity<ApiResponseDTO<PaymentDetailsResponseDto>> getPaymentDetail(
            @PathVariable("paymentId") UUID paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PaymentDetailsResponseDto paymentDetail = paymentService.getPaymentDetail(paymentId, userDetails.getMember());
        return ResponseEntity.ok(new ApiResponseDTO<>("success", paymentDetail));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @Operation(summary = "카드사에 결제 요청", description = "결제 요청하기")
    public ResponseEntity<ApiResponseDTO<String>> savePayment(
            @RequestBody PaymentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        paymentService.savePayment(requestDto, userDetails.getMember());
        return ResponseEntity.ok(new ApiResponseDTO<>("sucess", "결제가 완료되었습니다."));
    }


}
