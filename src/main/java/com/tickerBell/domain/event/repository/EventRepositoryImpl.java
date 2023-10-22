package com.tickerBell.domain.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.MainPageDto;
import com.tickerBell.domain.event.dtos.QEventListResponse;
import com.tickerBell.domain.event.entity.Category;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.tickerBell.domain.event.entity.QEvent.*;
import static com.tickerBell.domain.image.entity.QImage.*;

@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MainPageDto getMainPage() {
        // 뮤지컬 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> musicalEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.category.eq(Category.MUSICAL),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(5) // 5개만 추출
                .fetch();

        // 콘서트 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> concertEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.category.eq(Category.CONCERT),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(5) // 5개만 추출
                .fetch();

        // 공연 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> playEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.category.eq(Category.PLAY),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(5) // 5개만 추출
                .fetch();

        // 클래식 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> classicEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.category.eq(Category.CLASSIC),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(5) // 5개만 추출
                .fetch();

        // 스포츠 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> sportsEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.category.eq(Category.SPORTS),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(5) // 5개만 추출
                .fetch();

        // 세일 상위 조회수 5개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> saleEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url,
                        event.category
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.saleDegree.ne(0F), // sale 하지 않는 event 제외
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.startEvent.asc()) // 최근에 개봉 하는 순으로 정렬
                .limit(5) // 5개만 추출
                .fetch();

        // 마감 임박 상위 조회수 5개
        List<EventListResponse> deadLineEventList = queryFactory
                .select(new QEventListResponse(
                        event.id,
                        event.name,
                        event.startEvent,
                        image.s3Url,
                        event.category
                ))
                .from(event)
                .join(image).on(image.event.id.eq(event.id).and(image.isThumbnail.eq(true)))
                .where(
                        event.availablePurchaseTime.after(LocalDateTime.now())
                )
                .orderBy(event.availablePurchaseTime.asc()) // 최근에 개봉 하는 순으로 정렬
                .limit(5) // 5개만 추출
                .fetch();

        MainPageDto mainPageDto = MainPageDto.builder()
                .rankingMusicalEventList(musicalEventList)
                .rankingConcertEventList(concertEventList)
                .rankingPlayEventList(playEventList)
                .rankingClassicEventList(classicEventList)
                .rankingSportsEventList(sportsEventList)
                .saleEventList(saleEventList)
                .deadLineEventList(deadLineEventList)
                .recommendEventList(null) // todo: 추천 이벤트 추가해야 함
                .build();
        return mainPageDto;
    }
}
