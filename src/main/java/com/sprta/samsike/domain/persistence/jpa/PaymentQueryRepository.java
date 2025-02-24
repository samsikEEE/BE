package com.sprta.samsike.domain.persistence.jpa;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprta.samsike.domain.order.Payment;
import com.sprta.samsike.domain.order.QOrder;
import com.sprta.samsike.domain.order.QPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Payment> searchPayments(UUID restaurantId, String username, Integer minAmount, Integer maxAmount, String period, Pageable pageable) {
        QPayment payment = QPayment.payment;
        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();

        // 특정 레스토랑 내에서만 검색
        builder.and(order.restaurant.uuid.eq(restaurantId));

        // 고객 아이디(username)로 검색
        if (username != null && !username.isEmpty()) {
            builder.and(order.member.username.eq(username));
        }

        // 최소 금액 검색
        if (minAmount != null) {
            builder.and(payment.amount.goe(minAmount));
        }

        // 최대 금액 검색
        if (maxAmount != null) {
            builder.and(payment.amount.loe(maxAmount));
        }

        // 기간 별 조회 (최근 3일, 7일, 30일)
        if (period != null) {
            LocalDateTime now = LocalDateTime.now();
            switch (period) {
                case "3일":
                    builder.and(payment.createdAt.after(now.minusDays(3)));
                    break;
                case "7일":
                    builder.and(payment.createdAt.after(now.minusDays(7)));
                    break;
                case "30일":
                    builder.and(payment.createdAt.after(now.minusDays(30)));
                    break;
                default:
                    break;
            }
        }

        // Query 실행
        List<Payment> paymentList = queryFactory
                .selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .selectFrom(payment)
                .join(payment.order, order)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(paymentList, pageable, total);
    }
}
