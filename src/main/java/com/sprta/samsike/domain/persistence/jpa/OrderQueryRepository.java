package com.sprta.samsike.domain.persistence.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprta.samsike.domain.member.QMember;
import com.sprta.samsike.domain.order.Order;
import com.sprta.samsike.domain.order.QOrder;
import com.sprta.samsike.domain.order.QOrderProduct;
import com.sprta.samsike.domain.product.QProduct;
import com.sprta.samsike.domain.restaurant.QRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Order> searchOrders(String restaurantName, String menuName, Pageable pageable) {
        QOrder order = QOrder.order;
        QRestaurant restaurant = QRestaurant.restaurant;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();

        // 가게 이름 검색
        if (restaurantName != null && !restaurantName.isEmpty()) {
            builder.and(order.restaurant.uuid.in(
                    JPAExpressions.select(restaurant.uuid)
                            .from(restaurant)
                            .where(restaurant.name.containsIgnoreCase(restaurantName))
            ));
        }

        // 주문한 메뉴 이름 검색
        if (menuName != null && !menuName.isEmpty()) {
            builder.and(order.uuid.in(
                    JPAExpressions.select(orderProduct.order.uuid)
                            .from(orderProduct)
                            .join(orderProduct.product, product)
                            .where(product.name.containsIgnoreCase(menuName))
            ));
        }

        // Query 실행
        List<Order> orderList = queryFactory
                .selectFrom(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        long total = queryFactory
                .selectFrom(order)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(orderList, pageable, total);
    }


    // 관리자용 검색 조회
    public Page<Order> searchOrdersForAdmin(
            UUID restaurantId, String status, Integer minAmount, Integer maxAmount,
            String username, String menuName,
            Pageable pageable) {

        QOrder order = QOrder.order;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QProduct product = QProduct.product;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        // 특정 가게의 주문만 검색
        builder.and(order.restaurant.uuid.eq(restaurantId));

        // 주문 상태 필터링
        if (status != null && !status.isEmpty()) {
            builder.and(order.status.eq(status));
        }

        // 최소/최대 주문 금액 필터링
        if (minAmount != null) {
            builder.and(order.amount.goe(minAmount));
        }
        if (maxAmount != null) {
            builder.and(order.amount.loe(maxAmount));
        }

        // 고객 이름으로 검색
        if (username != null && !username.isEmpty()) {
            builder.and(order.member.username.containsIgnoreCase(username));
        }

        // 특정 메뉴가 포함된 주문 검색
        if (menuName != null && !menuName.isEmpty()) {
            builder.and(order.uuid.in(
                    JPAExpressions.select(orderProduct.order.uuid)
                            .from(orderProduct)
                            .join(orderProduct.product, product)
                            .where(product.name.containsIgnoreCase(menuName))
            ));
        }

        // Soft Delete 적용: 삭제되지 않은 주문만 검색
        builder.and(order.deletedBy.isNull());

        // Query 실행
        List<Order> orderList = queryFactory
                .selectFrom(order)
                .leftJoin(order.member, member).fetchJoin()
                .leftJoin(order.restaurant).fetchJoin()
                .where(builder)
                .orderBy(order.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        long total = queryFactory
                .selectFrom(order)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(orderList, pageable, total);
    }


}
