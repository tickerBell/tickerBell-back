package com.tickerBell.domain.event.service;

import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.category.repository.CategoryRepository;
import com.tickerBell.domain.event.dtos.EventListResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final Integer TOTALSEAT = 60; // 총 좌석 수 60으로 고정
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long saveEvent(Long memberId, String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, String host, String place, Integer age, String categoryName) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // [category] 추가
        // 유저가 선택한 category db에서 조회
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));


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
                .category(category) // [category] 추가
                .build();

        Event savedEvent = eventRepository.save(event);
        return savedEvent.getId();
    }

    @Override
    public List<EventListResponse> getEventByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        List<EventListResponse> eventListResponseList = eventRepository.findByCategoryName(category.getId()).stream()
                .map(event -> EventListResponse.from(event))
                .collect(Collectors.toList());
        return eventListResponseList;
    }
}

