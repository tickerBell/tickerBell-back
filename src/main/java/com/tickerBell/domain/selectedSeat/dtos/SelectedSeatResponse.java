package com.tickerBell.domain.selectedSeat.dtos;

import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedSeatResponse {
    private Long selectedSeatId;
    private String seatInfo; // 선택 좌석
    private Float seatPrice; // 할인을 적용한 선택 좌석 가격

    // SelectedSeat -> SelectedSeatResponse
    public static SelectedSeatResponse from(SelectedSeat selectedSeat) {
        return SelectedSeatResponse.builder()
                .selectedSeatId(selectedSeat.getId())
                .seatInfo(selectedSeat.getSeatInfo())
                .seatPrice(selectedSeat.getSeatPrice())
                .build();
    }
}
