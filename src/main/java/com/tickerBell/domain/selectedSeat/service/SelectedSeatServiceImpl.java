package com.tickerBell.domain.selectedSeat.service;

import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.selectedSeat.repository.SelectedSeatRepository;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SelectedSeatServiceImpl implements SelectedSeatService{
    private final SelectedSeatRepository selectedSeatRepository;
    @Transactional
    @Override
    public Integer saveSelectedSeat(List<SelectedSeat> selectedSeatList) {
        return selectedSeatRepository.saveAll(selectedSeatList).size();
    }

//    @Override
//    public List<SelectedSeatResponse> findSelectedSeatByTicketing(Long ticketingId) {
//        List<SelectedSeatResponse> selectedSeatResponseList = selectedSeatRepository.findByTicketingId(ticketingId).stream()
//                .map(selectedSeat -> SelectedSeatResponse.from(selectedSeat))
//                .collect(Collectors.toList());
//        return selectedSeatResponseList;
//    }
}
