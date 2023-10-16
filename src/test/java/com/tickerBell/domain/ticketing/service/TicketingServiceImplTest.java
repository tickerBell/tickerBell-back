package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatService;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatServiceImpl;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketingServiceImplTest {
    @InjectMocks
    private TicketingServiceImpl ticketingService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private SelectedSeatServiceImpl selectedSeatService;
    @Mock
    private TicketingRepository ticketingRepository;

    @Test
    @DisplayName("회원일 때 예매")
    public void saveTicketingMember() {
        // given
        Long memberId = 1L;
        Integer remainSeat = 30;
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("A-1", "B-2"))
                .build();
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(true, true, true);
        Event event = createEvent(remainSeat, 1000F, specialSeat);
        Ticketing ticketing = createTicketing(member, event);


        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));
        when(selectedSeatService.saveSelectedSeat(any())).thenReturn(2);

        // when
        ticketingService.saveTicketing(memberId, request);

        // then
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(eventRepository, times(1)).findById(any(Long.class));
        verify(ticketingRepository, times(1)).save(any(Ticketing.class));
        verify(selectedSeatService, times(2)).validCheckSeatInfo(any(), any());
        verify(selectedSeatService, times(1)).saveSelectedSeat(any());

        int expectedRemainSeat = remainSeat - request.getSelectedSeat().size();
        assertThat(expectedRemainSeat).isEqualTo(event.getRemainSeat());
    }

    @Test
    @DisplayName("이미 선택된좌석일 때 예외 처리")
    public void saveTicketingSAlreadySeatException() {
        // given
        Long memberId = 1L;
        Integer remainSeat = 30;
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("C-1", "C-2"))
                .build();
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(true, true, true);
        Event event = createEvent(remainSeat, 0, specialSeat);
        Ticketing ticketing = createTicketing(member, event);

        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doThrow(new CustomException(ErrorCode.ALREADY_SELECTED_SEAT))
                .when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));

        // then
        assertThatThrownBy(() -> ticketingService.saveTicketing(memberId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.ALREADY_SELECTED_SEAT.getErrorMessage());
    }

    @Test
    @DisplayName("옳바르지 않은 좌석 번호 예외 처리")
    public void saveTicketingNotValidSeatException() {
        // given
        Long memberId = 1L;
        Integer remainSeat = 30;
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("A-1", "D-1"))
                .build();
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, false);
        Event event = createEvent(remainSeat, 0.5F, specialSeat);
        Ticketing ticketing = createTicketing(member, event);

        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));

        // then
        assertThatThrownBy(() -> ticketingService.saveTicketing(memberId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SEAT_INFO_NOT_VALID_FORMAT.getErrorMessage());
    }

    @Test
    @DisplayName("옳바르지 않은 세일 예외 처리")
    public void saveTicketingNotValidSaleException() {
        // given
        Long memberId = 1L;
        Integer remainSeat = 30;
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("B-2", "C-1"))
                .build();
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, false);
        Event event = createEvent(remainSeat, -0.5F, specialSeat);
        Ticketing ticketing = createTicketing(member, event);

        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));

        // then
        assertThatThrownBy(() -> ticketingService.saveTicketing(memberId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SALE_DEGREE_NOT_VALID_FORMAT.getErrorMessage());
    }


    @Test
    @DisplayName("옳바르지 않은 세일 예외 처리")
    public void saveTicketingNonMember() {
        // given
        Long memberId = 1L;
        Integer remainSeat = 30;
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("B-2", "C-1"))
                .build();
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, false);
        Event event = createEvent(remainSeat, -0.5F, specialSeat);
        Ticketing ticketing = createTicketing(member, event);

        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));

        // then
        assertThatThrownBy(() -> ticketingService.saveTicketing(memberId, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.SALE_DEGREE_NOT_VALID_FORMAT.getErrorMessage());
    }








    private static Event createEvent(Integer remainSeat, float saleDegree, SpecialSeat specialSeat) {
        Event event = Event.builder()
                .name("eventName")
                .normalPrice(10000)
                .premiumPrice(15000)
                .totalSeat(60)
                .remainSeat(remainSeat)
                .saleDegree(saleDegree)
                .place("서울")
                .isAdult(true)
                .category(Category.CONCERT)
                .specialSeat(specialSeat)
                .build();
        return event;
    }

    private Ticketing createTicketing(Member member, Event event) {
        Ticketing ticketing = Ticketing.builder()
                .event(event)
                .member(member)
                .build();
        return ticketing;
    }

    private SpecialSeat createSpecialSeat(Boolean a, Boolean b, Boolean c) {
        SpecialSeat specialSeat = SpecialSeat.builder()
                .isSpecialSeatA(a)
                .isSpecialSeatB(b)
                .isSpecialSeatC(c)
                .build();
        return specialSeat;
    }

}