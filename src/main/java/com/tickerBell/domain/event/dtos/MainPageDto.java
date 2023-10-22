package com.tickerBell.domain.event.dtos;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
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

    @QueryProjection
    public MainPageDto(List<EventListResponse> rankingMusicalEventList, List<EventListResponse> rankingConcertEventList,
                       List<EventListResponse> rankingPlayEventList, List<EventListResponse> rankingClassicEventList,
                       List<EventListResponse> rankingSportsEventList, List<EventListResponse> saleEventList,
                       List<EventListResponse> deadLineEventList, List<EventListResponse> recommendEventList) {
        this.rankingMusicalEventList = rankingMusicalEventList;
        this.rankingConcertEventList = rankingConcertEventList;
        this.rankingPlayEventList = rankingPlayEventList;
        this.rankingClassicEventList = rankingClassicEventList;
        this.rankingSportsEventList = rankingSportsEventList;
        this.saleEventList = saleEventList;
        this.deadLineEventList = deadLineEventList;
        this.recommendEventList = recommendEventList;
    }
}
