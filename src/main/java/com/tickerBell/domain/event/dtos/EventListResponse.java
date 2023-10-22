package com.tickerBell.domain.event.dtos;

import com.querydsl.core.annotations.QueryProjection;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.image.dtos.ImageResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class EventListResponse {
    private Long eventId;
    private String eventName; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private String thumbNailImage; // 썸네일
    private Category category;

    //todo: 리스트에 반환할 데이터 더 필요하다면 추가

    public static EventListResponse from(Event event) {
        return EventListResponse.builder()
                .eventId(event.getId())
                .startEvent(event.getStartEvent())
                .eventName(event.getName())
                .thumbNailImage(null) //todo: 나중에 썸네일 이미지로 바꿔야함
                .build();
    }
    @QueryProjection
    public EventListResponse(Long eventId, String eventName, LocalDateTime startEvent, String thumbNailImage, Category category) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.startEvent = startEvent;
        this.thumbNailImage = thumbNailImage;
        this.category = category;
    }

    @QueryProjection
    public EventListResponse(Long eventId, String eventName, LocalDateTime startEvent, String thumbNailImage) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.startEvent = startEvent;
        this.thumbNailImage = thumbNailImage;
    }
}
