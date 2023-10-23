package com.tickerBell.domain.event.dtos;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tickerBell.domain.event.dtos.QMainPageDto is a Querydsl Projection type for MainPageDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMainPageDto extends ConstructorExpression<MainPageDto> {

    private static final long serialVersionUID = 1799154759L;

    public QMainPageDto(com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> rankingMusicalEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> rankingConcertEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> rankingPlayEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> rankingClassicEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> rankingSportsEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> saleEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> deadLineEventList, com.querydsl.core.types.Expression<? extends java.util.List<EventListResponse>> recommendEventList) {
        super(MainPageDto.class, new Class<?>[]{java.util.List.class, java.util.List.class, java.util.List.class, java.util.List.class, java.util.List.class, java.util.List.class, java.util.List.class, java.util.List.class}, rankingMusicalEventList, rankingConcertEventList, rankingPlayEventList, rankingClassicEventList, rankingSportsEventList, saleEventList, deadLineEventList, recommendEventList);
    }

}

