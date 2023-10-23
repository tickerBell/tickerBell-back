package com.tickerBell.domain.event.dtos;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tickerBell.domain.event.dtos.QEventListResponse is a Querydsl Projection type for EventListResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QEventListResponse extends ConstructorExpression<EventListResponse> {

    private static final long serialVersionUID = -1239210455L;

    public QEventListResponse(com.querydsl.core.types.Expression<Long> eventId, com.querydsl.core.types.Expression<String> eventName, com.querydsl.core.types.Expression<java.time.LocalDateTime> startEvent, com.querydsl.core.types.Expression<String> thumbNailImage, com.querydsl.core.types.Expression<com.tickerBell.domain.event.entity.Category> category) {
        super(EventListResponse.class, new Class<?>[]{long.class, String.class, java.time.LocalDateTime.class, String.class, com.tickerBell.domain.event.entity.Category.class}, eventId, eventName, startEvent, thumbNailImage, category);
    }

}

