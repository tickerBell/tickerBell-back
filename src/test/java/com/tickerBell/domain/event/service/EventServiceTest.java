//package com.tickerBell.domain.event.service;
//
//import com.tickerBell.domain.event.entity.Event;
//import com.tickerBell.domain.event.repository.EventRepository;
//import com.tickerBell.domain.member.entity.Member;
//import com.tickerBell.domain.member.repository.MemberRepository;
//import com.tickerBell.global.exception.CustomException;
//import com.tickerBell.global.exception.ErrorCode;
//import org.assertj.core.api.AbstractObjectAssert;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class EventServiceTest {
//
//    @InjectMocks
//    private EventServiceImpl eventService;
//    @Mock
//    private EventRepository eventRepository;
//    @Mock
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("이벤트 저장 테스트")
//    void saveEventTest() {
//        // given
//        Long memberId = 1L;
//        String name = "mockName";
//        LocalDateTime startEvent = LocalDateTime.now();
//        LocalDateTime endEvent = LocalDateTime.now();
//        Integer normalPrice = 100;
//        Integer premiumPrice = 1000;
//        Float saleDegree = 0.0F;
//        String casting = "mockCasting";
//        String host = "mockHost";
//        String place = "mockPlace";
//        Integer age = 18;
//
//        // stub
//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
//        when(eventRepository.save(any(Event.class))).thenReturn(Event.builder().build());
//
//        // when
//        Long savedEventId = eventService.saveEvent(memberId, name, startEvent, endEvent, normalPrice, premiumPrice, saleDegree, casting, host, place, age);
//
//        // then
//        verify(eventRepository, times(1)).save(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("이벤트 저장 회원조회 실패 테스트")
//    void saveEventMemberFailTest() {
//        // given
//        Long memberId = 1L;
//        String name = "mockName";
//        LocalDateTime startEvent = LocalDateTime.now();
//        LocalDateTime endEvent = LocalDateTime.now();
//        Integer normalPrice = 100;
//        Integer premiumPrice = 1000;
//        Float saleDegree = 0.0F;
//        String casting = "mockCasting";
//        String host = "mockHost";
//        String place = "mockPlace";
//        Integer age = 18;
//
//        // stub
//        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
//
//        // when
//        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(
//                () -> eventService.saveEvent(memberId, name, startEvent, endEvent, normalPrice, premiumPrice, saleDegree, casting, host, place, age))
//                .isInstanceOf(CustomException.class)
//                .extracting(ex -> (CustomException) ex);
//
//        // then
//        extracting.satisfies(ex -> {
//            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
//            assertThat(ex.getStatus()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getStatus().toString());
//            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getErrorMessage());
//        });
//    }
//}
