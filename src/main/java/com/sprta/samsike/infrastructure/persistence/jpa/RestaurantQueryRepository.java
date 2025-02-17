package com.sprta.samsike.infrastructure.persistence.jpa;

import ch.qos.logback.core.util.StringUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprta.samsike.application.dto.restaurant.RestaurantResponseDto;
import com.sprta.samsike.domain.restaurant.QCategory;
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
public class RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;
    QRestaurant restaurant = QRestaurant.restaurant;


    public Page<RestaurantResponseDto> getRestaurantList(String categoryId, String restaurantName,
                                                         String ssgCode, Pageable pageable,
                                                         String userName, String role) {

        QCategory category = QCategory.category1;

        BooleanExpression whereClause = buildWhereClause(categoryId, restaurantName, ssgCode, userName, role);

        List<RestaurantResponseDto> restaurantList =  queryFactory
                .select(Projections.constructor(RestaurantResponseDto.class,
                        restaurant.uuid,
                        restaurant.name,
                        restaurant.address,
                        restaurant.imageUrl,
                        restaurant.category.uuid,
                        restaurant.category.category,
                        restaurant.phone
                ))
                .from(restaurant)
                .innerJoin(restaurant.category, category)
                .where(whereClause)
                .fetch();


        long total = queryFactory
                .select(restaurant.count())
                .from(restaurant)
                .where(whereClause)
                .fetchOne();

        return new PageImpl<>(restaurantList, pageable, total);
    }


    private BooleanExpression buildWhereClause(String categoryId, String restaurantName,
                                               String ssgCode, String userName, String role) {

        BooleanExpression whereClause = restaurant.isNotNull();

        // 레스토랑 이름 필터링
        if (!StringUtil.isNullOrEmpty(restaurantName)) {
            whereClause = whereClause.and(restaurant.name.containsIgnoreCase(restaurantName));
        }

        // 카테고리 필터링
        if (!StringUtil.isNullOrEmpty(categoryId)) {
            whereClause = whereClause.and(restaurant.category.uuid.eq(UUID.fromString(categoryId)));
        }

        // 입력받은 SSG 코드 필터링
        if (!StringUtil.isNullOrEmpty(ssgCode)) {
            whereClause = whereClause.and(restaurant.sggCode.sggCd.eq(ssgCode));
        }

        return whereClause;
    }


}
