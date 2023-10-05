package com.tickerBell.domain.ticketing.dtos;

import com.tickerBell.domain.event.dtos.EventHistoryResponse;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingResponse {

    private Long ticketingId; // 예매 pk
    private Integer payment; // 지불 금액
    private List<SelectedSeatResponse> selectedSeatResponseList; // 선택한 좌석 정보 (위치, 가격)
    private EventHistoryResponse eventHistoryResponse; // 예매한 공연 정보
    private Boolean isPast; // 현재 시점으로부터 지난 예매인지 여부

    public static TicketingResponse from(Ticketing ticketing) {
        // selectedSeat dto 변환
        List<SelectedSeatResponse> selectedSeatResponseList = ticketing.getSelectedSeatList().stream()
                .map(selectedSeat -> SelectedSeatResponse.from(selectedSeat))
                .collect(Collectors.toList());
        // event dto 변환
        EventHistoryResponse eventHistoryResponse = EventHistoryResponse.from(ticketing.getEvent());

        // 공연 시간이 현재 시간보다 지난 공연인지 여부
        Boolean isPast = ticketing.getEvent().getStartEvent().isBefore(LocalDateTime.now());

        return TicketingResponse.builder()
                .ticketingId(ticketing.getId())
                .selectedSeatResponseList(selectedSeatResponseList)
                .eventHistoryResponse(eventHistoryResponse)
                .isPast(isPast)
                .build();
    }
}
