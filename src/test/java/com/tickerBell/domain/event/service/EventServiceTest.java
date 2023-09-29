package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이벤트 저장 테스트")
    void saveEventTest() {
        // given
        Long memberId = 1L;
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        String casting = "mockCasting";
        String host = "mockHost";
        String place = "mockPlace";
        Integer age = 18;

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(eventRepository.save(any(Event.class))).thenReturn(Event.builder().build());

        // when
        Long savedEventId = eventService.saveEvent(memberId, name, startEvent, endEvent, normalPrice, premiumPrice, saleDegree, casting, host, place, age);

        // then
        verify(eventRepository, times(1)).save(any(Event.class));
    }
}
