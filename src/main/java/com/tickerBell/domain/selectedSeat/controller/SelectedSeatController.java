package com.tickerBell.domain.selectedSeat.controller;

import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoRequest;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoResponse;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatService;
import com.tickerBell.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SelectedSeatController {
    private final SelectedSeatService selectedSeatService;

    @Operation(summary = "이벤트에 선택된 좌석 리스트 반환", description = "예매 시 이미 선택된 좌석들을 확인하기 위한 api")
    @PostMapping("/selected-seat/{eventId}")
    public ResponseEntity<Response> getSelectedSeatByEventId(@RequestBody SelectedSeatInfoRequest request) {
        List<SelectedSeatInfoResponse> selectedSeatInfoResponseList = selectedSeatService.getSelectedSeatByEventId(request);
        return ResponseEntity.ok(new Response(selectedSeatInfoResponseList, "해당 이벤트에 선택된 좌석 정보 반환"));
    }
}
