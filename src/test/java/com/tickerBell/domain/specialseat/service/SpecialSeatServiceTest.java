package com.tickerBell.domain.specialseat.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.repository.SpecialSeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialSeatServiceTest {

    @InjectMocks
    private SpecialSeatServiceImpl specialSeatService;
    @Mock
    private SpecialSeatRepository specialSeatRepository;
    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("특수석 저장 테스트")
    void saveSpecialSeatTest() {
        // given
        Long eventId = 1L;
        Boolean isSpecialSeatA = true;
        Boolean isSpecialSeatB = true;
        Boolean isSpecialSeatC = true;

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(Event.builder().build()));
        when(specialSeatRepository.save(any(SpecialSeat.class))).thenReturn(SpecialSeat.builder().build());

        // when
        specialSeatService.saveSpecialSeat(eventId, isSpecialSeatA, isSpecialSeatB, isSpecialSeatC);

        // then
        verify(eventRepository, times(1)).findById(eventId);
        verify(specialSeatRepository, times(1)).save(any(SpecialSeat.class));
    }
}
