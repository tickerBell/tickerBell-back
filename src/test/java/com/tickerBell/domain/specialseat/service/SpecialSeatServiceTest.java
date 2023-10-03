package com.tickerBell.domain.specialseat.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.repository.SpecialSeatRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        when(specialSeatRepository.save(any(SpecialSeat.class))).thenReturn(SpecialSeat.builder().build());

        // when
        specialSeatService.saveSpecialSeat(isSpecialSeatA, isSpecialSeatB, isSpecialSeatC);

        // then
        verify(eventRepository, times(1)).findById(eventId);
        verify(specialSeatRepository, times(1)).save(any(SpecialSeat.class));
    }


    // 이벤트 -> 특수성 단방향으로 변경
//    @Test
//    @DisplayName("특수석 저장 이벤트 조회 실패 테스트")
//    void saveSpecialSeatEventFailTest() {
//        // given
//        Long eventId = 1L;
//        Boolean isSpecialSeatA = true;
//        Boolean isSpecialSeatB = true;
//        Boolean isSpecialSeatC = true;
//
//        // stub
//        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
//
//        // when
//        AbstractObjectAssert<?, CustomException> extracting =
//                assertThatThrownBy(() -> specialSeatService.saveSpecialSeat(eventId, isSpecialSeatA, isSpecialSeatB, isSpecialSeatC))
//                        .isInstanceOf(CustomException.class)
//                        .extracting(ex -> (CustomException) ex);
//
//        // then
//        extracting.satisfies(ex -> {
//            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_NOT_FOUND);
//            assertThat(ex.getStatus()).isEqualTo(ErrorCode.EVENT_NOT_FOUND.getStatus().toString());
//            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.EVENT_NOT_FOUND.getErrorMessage());
//        });
//    }
}
