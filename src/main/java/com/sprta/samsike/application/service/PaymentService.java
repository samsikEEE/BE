package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.order.OrderResponseDto;
import com.sprta.samsike.application.dto.payment.PaymentDetailsResponseDto;
import com.sprta.samsike.application.dto.payment.PaymentRequestDto;
import com.sprta.samsike.application.dto.payment.PaymentResponseDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.order.Payment;
import com.sprta.samsike.infrastructure.persistence.jpa.OrderRepository;
import com.sprta.samsike.infrastructure.persistence.jpa.PaymentRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getPayment(Member member, int page, int size, String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        MemberRoleEnum memberRoleEnum = MemberRoleEnum.valueOf(member.getRole());

        Page<Payment> paymentList;

        if (memberRoleEnum == MemberRoleEnum.ROLE_OWNER) {
            // OWNER는 자신이 관리하는 가게의 모든 결제 조회
            paymentList = paymentRepository.findByOrderRestaurantMember(member, pageable);
        } else {
            // CUSTOMER는 자신의 결제 내역만 조회
            paymentList = paymentRepository.findByOrderMember(member, pageable);
        }
        return paymentList.map(PaymentResponseDto::new);

    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getRestaurantPayments(Member member, UUID restaurantId, int page, int size, String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (restaurantId == null) {
            throw new IllegalArgumentException("MASTER, MANAGER는 특정 가게의 결제만 조회할 수 있습니다. restaurantId를 제공하세요.");
        }

        // 권한 체크
        MemberRoleEnum memberRoleEnum = MemberRoleEnum.valueOf(member.getRole());
        if (memberRoleEnum != MemberRoleEnum.ROLE_MANAGER && memberRoleEnum != MemberRoleEnum.ROLE_MASTER) {
            throw new CustomException(ErrorCode.PAYMENT002, "해당 결제 내역을 조회할 권한이 없습니다.");
        }

        Page<Payment> paymentList = paymentRepository.findByOrderRestaurantUuid(restaurantId, pageable);


        return paymentList.map(PaymentResponseDto::new);
    }



    @Transactional
    public void savePayment(PaymentRequestDto requestDto, Member member) {

        // 주문 조회
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER001, "주문을 찾을 수 없습니다."));

        if (order.getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.PAYMENT003, "본인의 주문이 아닙니다.");
        }

        // 결제 정보 저장
        Payment payment = new Payment(
                order,
                "카드",
                requestDto.getTransactionId(),
                requestDto.getCardNumberMasked()
        );

        paymentRepository.save(payment);

    }

    @Transactional(readOnly = true)
    public PaymentDetailsResponseDto getPaymentDetail(UUID paymentId, Member member) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT001, "결제 정보를 찾을 수 없습니다."));

        // 본인의 주문이거나 관리자인 경우 조회 허용
        if (!payment.getOrder().getMember().getUsername().equals(member.getUsername()) &&
                !payment.getOrder().getRestaurant().getMember().getUsername().equals(member.getUsername())) {
            throw new CustomException(ErrorCode.PAYMENT002, "결제 정보를 조회할 권한이 없습니다.");
        }

        return new PaymentDetailsResponseDto(payment);

    }
}
