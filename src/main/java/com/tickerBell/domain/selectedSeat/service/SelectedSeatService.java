package com.tickerBell.domain.selectedSeat.service;

import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;

import java.util.List;

public interface SelectedSeatService {
    Integer saveSelectedSeat(List<SelectedSeat> selectedSeatList);
    void validCheckSeatInfo(Long eventId, String seatInfo);
}
