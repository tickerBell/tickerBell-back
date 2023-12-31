package com.tickerBell.domain.ticketing.dtos;

import com.tickerBell.domain.event.dtos.EventHistoryUserResponse;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
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
    private Float payment; // 총 지불 금액 (좌석이 2개라면 2개의 합)
    private List<SelectedSeatResponse> selectedSeatResponseList; // 선택한 좌석 정보 (위치, 가격)
    private EventHistoryUserResponse eventHistoryResponse; // 예매한 공연 정보
    private LocalDateTime selectedDate; // 예매 날짜
    private String paymentId; // 구매 식별 번호
    private Boolean isPast; // 현재 시점으로부터 지난 예매인지 여부
    private Boolean isTicketingCancelled; // 취소한 예매인지 여부


    public static TicketingResponse from(Ticketing ticketing) {
        // selectedSeat dto 변환
        List<SelectedSeatResponse> selectedSeatResponseList = ticketing.getSelectedSeatList().stream()
                .map(selectedSeat -> SelectedSeatResponse.from(selectedSeat))
                .collect(Collectors.toList());
        Float payment = 0F;
        for (SelectedSeat selectedSeat : ticketing.getSelectedSeatList()) {
            payment += selectedSeat.getSeatPrice();
        }
        // event dto 변환
        EventHistoryUserResponse eventHistoryResponse = EventHistoryUserResponse.from(ticketing.getEvent());

        // 공연 시간이 현재 시간보다 지난 공연인지 여부
        Boolean isPast = ticketing.getEvent().getStartEvent().isBefore(LocalDateTime.now());

        return TicketingResponse.builder()
                .ticketingId(ticketing.getId())
                .selectedSeatResponseList(selectedSeatResponseList)
                .eventHistoryResponse(eventHistoryResponse)
                .payment(payment)
                .selectedDate(ticketing.getSelectedDate())
                .paymentId(ticketing.getPaymentId())
                .isPast(isPast)
                .isTicketingCancelled(ticketing.getIsDelete())
                .build();
    }
}
