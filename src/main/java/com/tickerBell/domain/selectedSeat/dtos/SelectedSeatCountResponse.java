package com.tickerBell.domain.selectedSeat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SelectedSeatCountResponse {
    private Integer selectedSeatCount; // 예매된 좌석 수 (특정 일)
}
