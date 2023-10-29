package com.tickerBell.domain.event.dtos;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageDto {

    // 카테고리별 랭킹
    private List<EventListResponse> rankingMusicalEventList;
    private List<EventListResponse> rankingConcertEventList;
    private List<EventListResponse> rankingPlayEventList;
    private List<EventListResponse> rankingClassicEventList;
    private List<EventListResponse> rankingSportsEventList;

    // 세일 이벤트
    private List<EventListResponse> saleEventList;

    // 마감 임박 이벤트
    private List<EventListResponse> deadLineEventList;

    // 추천 이벤트
    private List<EventListResponse> recommendEventList;
}
