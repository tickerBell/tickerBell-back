package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventHistoryResponse {
    /**
     * 예매 내역 볼 때 필요한 이벤트 정보 반환 클래스
     */
    private Long eventId;
    private String name; // 이벤트 명
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private String place; // 이벤트 장소

    public static EventHistoryResponse from(Event event) {
        return EventHistoryResponse.builder()
                .eventId(event.getId())
                .name(event.getName())
                .startEvent(event.getStartEvent())
                .place(event.getPlace())
                .build();
    }
}
