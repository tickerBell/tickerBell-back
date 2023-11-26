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
public class SelectedSeatInfoResponse {
    private String selectedSeat; // 선택된 좌석

    public static SelectedSeatInfoResponse from(SelectedSeat selectedSeat) {
        return SelectedSeatInfoResponse.builder()
                .selectedSeat(selectedSeat.getSeatInfo())
                .build();
    }
}
