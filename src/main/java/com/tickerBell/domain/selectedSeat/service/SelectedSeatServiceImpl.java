package com.tickerBell.domain.selectedSeat.service;

import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatCountResponse;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoRequest;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatInfoResponse;
import com.tickerBell.domain.selectedSeat.dtos.SelectedSeatResponse;
import com.tickerBell.domain.selectedSeat.repository.SelectedSeatRepository;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SelectedSeatServiceImpl implements SelectedSeatService{
    private final SelectedSeatRepository selectedSeatRepository;

    // 예매하며 선택된 좌석 저장
    @Transactional
    @Override
    public Integer saveSelectedSeat(List<SelectedSeat> selectedSeatList) {
        return selectedSeatRepository.saveAll(selectedSeatList).size();
    }

    // 이미 선택된 좌석인지 체크
    @Override
    public void validCheckSeatInfo(Long eventId, String seatInfo, LocalDateTime selectedDate) {
        Optional<SelectedSeat> findSelectedSeat = selectedSeatRepository.findByEventIdAndSeatInfo(eventId, seatInfo, selectedDate);

        if (findSelectedSeat.isPresent()) {
            log.info("이미 선택된 좌석이 선택됨");
            throw new CustomException(ErrorCode.ALREADY_SELECTED_SEAT, findSelectedSeat.get().getSeatInfo() + "은 이미 선택된 좌석 입니다.");
        }
    }

    @Override
    public List<SelectedSeatInfoResponse> getSelectedSeatByEventId(SelectedSeatInfoRequest request) {
        return selectedSeatRepository.findByEventId(request.getEventId(), request.getSelectedDate()).stream()
                .map(selectedSeat -> SelectedSeatInfoResponse.from(selectedSeat))
                .collect(Collectors.toList());
    }

    @Override
    public SelectedSeatCountResponse getSelectedSeatCount(SelectedSeatInfoRequest request) {
        Integer selectedSeatCount = selectedSeatRepository.findSelectedSeatCount(request.getEventId(), request.getSelectedDate());
        return new SelectedSeatCountResponse(selectedSeatCount);
    }
}
