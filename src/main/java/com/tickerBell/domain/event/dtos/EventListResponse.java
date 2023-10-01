package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.image.dtos.ImageResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventListResponse {
    private Long eventId;
    private String eventName; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private ImageResponse thumbNailImage; // 썸네일

    //todo: 리스트에 반환할 데이터 더 필요하다면 추가

    public static EventListResponse from(Event event) {
        return EventListResponse.builder()
                .eventId(event.getId())
                .startEvent(event.getStartEvent())
                .eventName(event.getName())
                .thumbNailImage(null) //todo: 나중에 썸네일 이미지로 바꿔야함
                .build();

    }
}
