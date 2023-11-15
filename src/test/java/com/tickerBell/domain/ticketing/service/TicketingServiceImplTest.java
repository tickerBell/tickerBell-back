package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.NonMember;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.member.repository.NonMemberRepository;
import com.tickerBell.domain.selectedSeat.service.SelectedSeatServiceImpl;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberCancelRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private NonMemberRepository nonMemberRepository;
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
    @DisplayName("이전에 예매 이력이 있는 비회원일 때 예매")
    public void saveTicketingNonMemberAgain() {
        // given
        Integer remainSeat = 30;
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .name("비회원 이름")
                .phone("01012345678")
                .selectedSeat(List.of("C-2", "C-3"))
                .eventId(1L)
                .build();
        NonMember nonMember = NonMember.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, false);
        Event event = createEvent(remainSeat, 1000, specialSeat);
        Ticketing ticketing = createTicketingNonMember(nonMember, event);

        // stub
        when(nonMemberRepository.findByNameAndPhone(any(String.class), any(String.class))).thenReturn(Optional.of(nonMember));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));
        when(selectedSeatService.saveSelectedSeat(any())).thenReturn(2);

        // when
        ticketingService.saveTicketingNonMember(request);

        // then
        verify(nonMemberRepository, times(1)).findByNameAndPhone(any(String.class), any(String.class));
        verify(nonMemberRepository, times(0)).save(any(NonMember.class));
        verify(eventRepository, times(1)).findById(any(Long.class));
        verify(ticketingRepository, times(1)).save(any(Ticketing.class));
        verify(selectedSeatService, times(2)).validCheckSeatInfo(any(), any());
        verify(selectedSeatService, times(1)).saveSelectedSeat(any());

        int expectedRemainSeat = remainSeat - request.getSelectedSeat().size();
        assertThat(expectedRemainSeat).isEqualTo(event.getRemainSeat());
    }

    @Test
    @DisplayName("이전에 예매 이력이 없는 비회원일 때 예매")
    public void saveTicketingNonMemberNew() {
        // given
        Integer remainSeat = 30;
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .name("비회원 이름")
                .phone("01012345678")
                .selectedSeat(List.of("B-2", "C-3"))
                .eventId(1L)
                .build();
        NonMember nonMember = NonMember.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, true);
        Event event = createEvent(remainSeat, 1000, specialSeat);
        Ticketing ticketing = createTicketingNonMember(nonMember, event);

        // stub
        when(nonMemberRepository.findByNameAndPhone(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(event));
        when(ticketingRepository.save(any(Ticketing.class))).thenReturn(ticketing);
        doNothing().when(selectedSeatService).validCheckSeatInfo(any(), any(String.class));
        when(selectedSeatService.saveSelectedSeat(any())).thenReturn(2);

        // when
        ticketingService.saveTicketingNonMember(request);

        // then
        verify(nonMemberRepository, times(1)).findByNameAndPhone(any(String.class), any(String.class));
        verify(nonMemberRepository, times(1)).save(any(NonMember.class));
        verify(eventRepository, times(1)).findById(any(Long.class));
        verify(ticketingRepository, times(1)).save(any(Ticketing.class));
        verify(selectedSeatService, times(2)).validCheckSeatInfo(any(), any());
        verify(selectedSeatService, times(1)).saveSelectedSeat(any());

        int expectedRemainSeat = remainSeat - request.getSelectedSeat().size();
        assertThat(expectedRemainSeat).isEqualTo(event.getRemainSeat());
    }

    @Test
    @DisplayName("회원일 때 예매 내역 가져오기")
    public void getTicketingHistory() {
        // given
        Long memberId = 1L;
        Member member = Member.builder().build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, true);
        Event event = createEvent(30, 1000, specialSeat);
        Ticketing ticketing = createTicketing(member, event);
        List<Ticketing> ticketingList = new ArrayList<>();
        ticketingList.add(ticketing);
        Page<Ticketing> ticketingPage = new PageImpl<>(ticketingList);

        // stub
        Member spyMember = spy(member);

        when(spyMember.getId()).thenReturn(1L);
        when(memberRepository.findById(any())).thenReturn(Optional.of(spyMember));
        when(ticketingRepository.findByMemberId(any(Long.class), PageRequest.of(0, 10))).thenReturn(ticketingPage);

        // when
        Page<TicketingResponse> ticketingResponseList = ticketingService.getTicketingHistory(memberId, 0, 10);

        // then
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(ticketingRepository, times(1)).findByMemberId(any(Long.class), PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("비회원일 때 예매 내역 가져오기")
    public void getTicketingHistoryNonMember() {
        // given
        NonMember nonMember = NonMember.builder().name("이름").phone("01012345678").build();
        SpecialSeat specialSeat = createSpecialSeat(false, false, true);
        Event event = createEvent(30, 1000, specialSeat);
        Ticketing ticketing = createTicketingNonMember(nonMember, event);
        List<Ticketing> ticketingList = new ArrayList<>();
        ticketingList.add(ticketing);
        Page<Ticketing> ticketingPage = new PageImpl<>(ticketingList);

        // stub
        NonMember spyNonMember = spy(nonMember);
        when(spyNonMember.getId()).thenReturn(1L);
        when(nonMemberRepository.findByNameAndPhone(any(String.class), any(String.class))).thenReturn(Optional.of(spyNonMember));
        when(ticketingRepository.findByNonMemberId(any(Long.class), any(PageRequest.class))).thenReturn(ticketingPage);

        // when
        Page<TicketingResponse> ticketingResponseList = ticketingService.getTicketingHistoryNonMember(nonMember.getName(), nonMember.getPhone(), 0, 10);

        // then
        verify(nonMemberRepository, times(1)).findByNameAndPhone(any(String.class), any(String.class));
        verify(ticketingRepository, times(1)).findByNonMemberId(any(Long.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("회원일 때 예매 내역 취소")
    public void cancelTicketing() {
        // given
        Long memberId = 1L;
        Long ticketingId = 1L;
        Member member = Member.builder().build();
        Event event = Event.builder()
                .remainSeat(30)
                .build();
        Ticketing ticketing = createTicketing(member, event);

        // stub
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(ticketingRepository.findByIdWithEvent(any(Long.class))).thenReturn(Optional.of(ticketing));
        doNothing().when(ticketingRepository).delete(any(Ticketing.class));

        // when
        ticketingService.cancelTicketing(memberId, ticketingId);

        // then
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(ticketingRepository, times(1)).findByIdWithEvent(any(Long.class));
    }

    @Test
    @DisplayName("비회원일 때 예매 내역 취소")
    public void cancelTicketingNonMember() {
        // given
        Long ticketingId = 1L;
        Event event = Event.builder()
                .remainSeat(30)
                .build();
        Ticketing ticketing = createTicketing(null, event);
        TicketingNonMemberCancelRequest request = TicketingNonMemberCancelRequest.builder()
                .name("nonMember").phone("01012345678").build();

        // stub
        when(ticketingRepository.findByIdAndNonMemberWithEvent(any(Long.class), any(String.class), any(String.class)))
                .thenReturn(Optional.of(ticketing));
        doNothing().when(ticketingRepository).delete(any(Ticketing.class));

        // when
        ticketingService.cancelTicketingNonMember(request, ticketingId);

        // then
        verify(ticketingRepository, times(1)).findByIdAndNonMemberWithEvent(any(Long.class), any(String.class), any(String.class));
    }


    private static Event createEvent(Integer remainSeat, float saleDegree, SpecialSeat specialSeat) {
        Event event = Event.builder()
                .name("eventName")
                .normalPrice(10000)
                .premiumPrice(15000)
                .startEvent(LocalDateTime.now())
                .endEvent(LocalDateTime.now())
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

    private Ticketing createTicketingNonMember(NonMember nonMember, Event event) {
        Ticketing ticketing = Ticketing.builder()
                .event(event)
                .nonMember(nonMember)
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