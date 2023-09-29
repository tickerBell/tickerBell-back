package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final Integer TOTALSEAT = 60; // 총 좌석 수 60으로 고정
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long saveEvent(Long memberId, String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, String host, String place, Integer age) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Event event = Event.builder()
                .name(name)
                .startEvent(startEvent)
                .endEvent(endEvent)
                .normalPrice(normalPrice)
                .premiumPrice(premiumPrice)
                .saleDegree(saleDegree)
                .casting(casting)
                .totalSeat(TOTALSEAT)
                .remainSeat(TOTALSEAT) // remainSeat 는 등록 시 totalSeat 와 같다고 구현
                .host(host)
                .place(place)
                .member(findMember)
                .build();

        Event savedEvent = eventRepository.save(event);
        return savedEvent.getId();
    }
}

