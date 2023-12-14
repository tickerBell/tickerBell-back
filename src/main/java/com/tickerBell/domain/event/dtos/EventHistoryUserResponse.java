package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventHistoryUserResponse {
    /**
     * 예매자 회원 myPage 조회 시 예매 내역 리스트에 사용 되는 event dto
     */
    private Long eventId;
    private String eventName; // 이벤트 명
    private String place; // 이벤트 장소
    private List<String> castingList; // 캐스팅 목록
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private LocalDateTime endEvent; // 이벤트 종료 시간
    public static EventHistoryUserResponse from(Event event) {
        List<String> castingList = new ArrayList<>();
        for (Casting casting : event.getCastingList()) {
            castingList.add(casting.getCastingName());
        }

        return EventHistoryUserResponse.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .place(event.getPlace())
                .castingList(castingList)
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .build();
    }
}
