package com.tickerBell.domain.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tickerBell.domain.casting.entity.QCasting;
import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.MainPageDto;
import com.tickerBell.domain.event.dtos.QEventListResponse;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.tickerBell.domain.casting.entity.QCasting.*;
import static com.tickerBell.domain.event.entity.QEvent.*;
import static com.tickerBell.domain.image.entity.QImage.*;
import static com.tickerBell.domain.member.entity.QMember.member;
import static com.tickerBell.domain.specialseat.entity.QSpecialSeat.specialSeat;

@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MainPageDto getMainPage() {
        // 뮤지컬 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> musicalEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.category.eq(Category.MUSICAL),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(7) // 7개만 추출
                .fetch();

        // 콘서트 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> concertEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.category.eq(Category.CONCERT),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(7) // 7개만 추출
                .fetch();

        // 공연 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> playEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.category.eq(Category.PLAY),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(7) // 7개만 추출
                .fetch();

        // 클래식 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> classicEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.category.eq(Category.CLASSIC),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(7) // 7개만 추출
                .fetch();

        // 스포츠 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> sportsEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.category.eq(Category.SPORTS),
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.viewCount.desc())
                .limit(7) // 7개만 추출
                .fetch();

        // 세일 상위 조회수 7개 (공연 예정인 이벤트에 대해서)
        List<EventListResponse> saleEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.saleDegree.ne(0F), // sale 하지 않는 event 제외
                        event.startEvent.after(LocalDateTime.now())
                )
                .orderBy(event.startEvent.asc()) // 최근에 개봉 하는 순으로 정렬
                .limit(7) // 7개만 추출
                .fetch();

        // 마감 임박 상위 조회수 7개
        List<EventListResponse> deadLineEventList = queryFactory
                .select(new QEventListResponse(
                        event
                ))
                .from(event)
                .where(
                        event.availablePurchaseTime.after(LocalDateTime.now())
                )
                .orderBy(event.availablePurchaseTime.asc()) // 최근에 개봉 하는 순으로 정렬
                .limit(7) // 5개만 추출ㅋㅋ
                .fetch();


        MainPageDto mainPageDto = MainPageDto.builder()
                .rankingMusicalEventList(musicalEventList)
                .rankingConcertEventList(concertEventList)
                .rankingPlayEventList(playEventList)
                .rankingClassicEventList(classicEventList)
                .rankingSportsEventList(sportsEventList)
                .saleEventList(saleEventList)
                .openAscEventList(deadLineEventList)
                .recommendEventList(null) // todo: 추천 이벤트 추가해야 함
                .build();
        return mainPageDto;
    }

    @Override
    public Page<Event> findByMemberIdFetchAllPage(Long memberId, Pageable pageable) {
        List<Event> events = queryFactory.select(event)
                .from(event)
                .join(event.member, member).fetchJoin()
                .join(event.specialSeat, specialSeat).fetchJoin()
                .where(event.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(event.count())
                .from(event)
                .where(event.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(events, pageable, count);
    }

    @Override
    public Page<Event> findByCategoryFetchAllPage(Category category, Pageable pageable) {
        List<Event> events = queryFactory.select(event)
                .from(event)
                .join(event.member, member).fetchJoin()
                .join(event.specialSeat, specialSeat).fetchJoin()
                .where(event.category.eq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(event.count())
                .from(event)
                .where(event.category.eq(category))
                .fetchOne();

        return new PageImpl<>(events, pageable, count);
    }
}
