package com.tickerBell.domain.event.dtos;

import com.querydsl.core.annotations.QueryProjection;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.utils.SeatPriceCalculator;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListResponse {
    private Long eventId;
    private String eventName; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private String thumbNailImage; // 썸네일
    private Integer normalPrice; // 일반 좌석 가격
    private Integer premiumPrice; // 특수석 좌석 가격
    private Float saleDegree; // 세일
    private Float afterSalePrice; // 세일 후 가격
    private Float afterSalePremiumPrice; // 세일 후 특수석 좌석 가격
    private Category category;

    //todo: 리스트에 반환할 데이터 더 필요하다면 추가

    public static EventListResponse from(Event event) {
        return EventListResponse.builder()
                .eventId(event.getId())
                .startEvent(event.getStartEvent())
                .eventName(event.getName())
                .thumbNailImage(null) //todo: 나중에 썸네일 이미지로 바꿔야함
                .normalPrice(event.getNormalPrice())
                .saleDegree(event.getSaleDegree())
                .afterSalePrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getNormalPrice()))
                .afterSalePremiumPrice(SeatPriceCalculator.getSeatPrice(event.getSaleDegree(), event.getPremiumPrice()))
                .build();
    }

    @QueryProjection
    public EventListResponse(Long eventId, String eventName, LocalDateTime startEvent, String thumbNailImage, Integer normalPrice, Float saleDegree, Category category) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.startEvent = startEvent;
        this.thumbNailImage = thumbNailImage;
        this.normalPrice = normalPrice;
        this.saleDegree = saleDegree;
        this.category = category;
    }
}
