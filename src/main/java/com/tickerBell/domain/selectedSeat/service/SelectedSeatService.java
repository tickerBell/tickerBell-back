package com.tickerBell.domain.selectedSeat.service;

import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoRequest;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoResponse;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;

import java.time.LocalDateTime;
import java.util.List;

public interface SelectedSeatService {
    Integer saveSelectedSeat(List<SelectedSeat> selectedSeatList);
    void validCheckSeatInfo(Long eventId, String seatInfo, LocalDateTime selectedDate);
    List<SelectedSeatInfoResponse> getSelectedSeatByEventId(SelectedSeatInfoRequest request);
}
