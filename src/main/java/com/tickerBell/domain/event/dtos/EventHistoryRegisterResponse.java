package com.tickerBell.domain.event.dtos;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class EventHistoryRegisterResponse {
    /**
     * 등록자 회원 myPage 조회 시 등록 event list 에 사용 되는 event dto
     */
    private Long eventId;
    private String eventName; // 이벤트 명
    private String place; // 이벤트 장소
    private List<String> castingList; // 캐스팅 목록
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private LocalDateTime endEvent; // 이벤트 종료 시간

    private Integer selectedSeatCount; // 예매된 좌석 수 (이벤트 전체 기간)
    private Boolean isEventCancelled; // 이벤트 취소 여부

    public static EventHistoryRegisterResponse from(Event event) {
        List<String> castingList = new ArrayList<>();
        for (Casting casting : event.getCastingList()) {
            castingList.add(casting.getCastingName());
        }
        int selectedSeatCount = 0;
        for (Ticketing ticketing : event.getTicketingList()) {
            selectedSeatCount += ticketing.getSelectedSeatList().size();
        }

        return EventHistoryRegisterResponse.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .place(event.getPlace())
                .castingList(castingList)
                .startEvent(event.getStartEvent())
                .endEvent(event.getEndEvent())
                .selectedSeatCount(selectedSeatCount)
                .isEventCancelled(event.getIsCancelled())
                .build();
    }
}
