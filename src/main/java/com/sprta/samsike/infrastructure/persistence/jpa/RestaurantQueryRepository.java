package com.sprta.samsike.infrastructure.persistence.jpa;

import ch.qos.logback.core.util.StringUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprta.samsike.application.dto.restaurant.RestaurantDetailResponseDto;
import com.sprta.samsike.application.dto.restaurant.RestaurantResponseDto;
import com.sprta.samsike.domain.restaurant.QCategory;
import com.sprta.samsike.domain.restaurant.QRestaurant;
import com.sprta.samsike.domain.restaurant.Restaurant;
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


    public Page<RestaurantResponseDto> getRestaurantList(String categoryName, String restaurantName,
                                                         String ssgCode, Pageable pageable) {

        QCategory category = QCategory.category1;

        BooleanExpression whereClause = buildWhereClause(categoryName, restaurantName, ssgCode);

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
                .orderBy(restaurant.createdAt.desc(), restaurant.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = queryFactory
                .select(restaurant.count())
                .from(restaurant)
                .where(whereClause)
                .fetchOne();

        return new PageImpl<>(restaurantList, pageable, total);
    }


    private BooleanExpression buildWhereClause(String categoryName, String restaurantName,
                                               String ssgCode) {

        BooleanExpression whereClause = restaurant.isNotNull().and(restaurant.deletedAt.isNull());

        // 레스토랑 이름 필터링
        if (!StringUtil.isNullOrEmpty(restaurantName)) {
            whereClause = whereClause.and(restaurant.name.containsIgnoreCase(restaurantName));
        }

        // 카테고리 필터링
        if (!StringUtil.isNullOrEmpty(categoryName)) {
            whereClause = whereClause.and(restaurant.category.category.contains(categoryName));
        }

        // 입력받은 SSG 코드 필터링
        if (!StringUtil.isNullOrEmpty(ssgCode)) {
            whereClause = whereClause.and(restaurant.sggCode.sggCd.eq(ssgCode));
        }

        return whereClause;
    }



}
