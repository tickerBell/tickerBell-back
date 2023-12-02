package com.tickerBell.domain.selectedSeat.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SelectedSeatInfoRequest {
    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId;
    @Schema(description = "예매 날짜", example = "2023-12-30T00:00:00Z")
    private LocalDateTime selectedDate;
}
