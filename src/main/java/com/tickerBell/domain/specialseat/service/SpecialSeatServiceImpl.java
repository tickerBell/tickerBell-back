package com.tickerBell.domain.specialseat.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.repository.SpecialSeatRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialSeatServiceImpl implements SpecialSeatService {

    private final SpecialSeatRepository specialSeatRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public SpecialSeat saveSpecialSeat(Boolean isSpecialSeatA, Boolean isSpecialSeatB, Boolean isSpecialSeatC) {
        SpecialSeat specialSeat = SpecialSeat.builder()
                .isSpecialSeatA(isSpecialSeatA)
                .isSpecialSeatB(isSpecialSeatB)
                .isSpecialSeatC(isSpecialSeatC)
                .build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);

        return savedSpecialSeat;
    }
}
