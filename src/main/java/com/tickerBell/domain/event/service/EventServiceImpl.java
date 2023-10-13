package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.EventResponse;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.host.repository.HostRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.service.SpecialSeatService;
import com.tickerBell.domain.tag.entity.Tag;
import com.tickerBell.domain.tag.service.TagService;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
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
    private final SpecialSeatService specialSeatService;
    private final TagService tagService;
    private final HostRepository hostRepository;

    @Override
    @Transactional
    public Long saveEvent(Long memberId, SaveEventRequest request) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 특수석 정보 저장
        SpecialSeat specialSeat = specialSeatService.saveSpecialSeat(request.getIsSpecialA(), request.getIsSpecialB(), request.getIsSpecialC());

        LocalDateTime availablePurchaseTime = checkAvailablePurchaseTime(request.getAvailablePurchaseTime(), request.getStartEvent());

        // event 저장
        Event event = Event.builder()
                .name(request.getName())
                .startEvent(request.getStartEvent())
                .endEvent(request.getEndEvent())
                .availablePurchaseTime(availablePurchaseTime)
                .normalPrice(request.getNormalPrice())
                .premiumPrice(request.getPremiumPrice())
                .saleDegree(request.getSaleDegree())
                .casting(request.getCasting())
                .totalSeat(TOTALSEAT)
                .remainSeat(TOTALSEAT) // remainSeat 는 등록 시 totalSeat 와 같다고 구현
                .isAdult(request.getIsAdult())
                .place(request.getPlace())
                .category(request.getCategory())
                .member(findMember) // member 연관관계
                .specialSeat(specialSeat) // special seat 연관관계
                .build();

        // tag 저장
        List<String> tagNameList = request.getTags();
        List<Tag> tagList = tagNameList.stream()
                .map(tagName -> new Tag(tagName, event, findMember))
                .collect(Collectors.toList());

        Integer savedTagSize = tagService.saveTagList(tagList);
        log.info("저장된 태그 수: " + savedTagSize);

        List<String> hosts = request.getHosts();
        for (String host : hosts) {
            hostRepository.save(Host.builder().hostName(host).event(event).build());
        }

        return eventRepository.save(event).getId();
    }

    private LocalDateTime checkAvailablePurchaseTime(LocalDateTime availablePurchaseTime, LocalDateTime startTime) {
        if (availablePurchaseTime == null) {
            return startTime.minus(Period.ofWeeks(2)); // 구매 가능 시간이 없으면 시작 시간에서 2주 전 시간 반환
        } else {
            return availablePurchaseTime; // 구매 가능 시간이 존재하면 바로 리턴
        }
    }

    @Override
    public List<EventListResponse> getEventByCategory(Category category) {
        List<Event> findEventsByCategory = eventRepository.findByCategory(category);
        List<EventListResponse> responses = new ArrayList<>();
        for (Event event : findEventsByCategory) {
            EventListResponse response = EventListResponse.from(event);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public EventResponse findByIdFetchAll(Long eventId) {
        Event findEvent = eventRepository.findByIdFetchAll(eventId);
        EventResponse response = EventResponse.from(findEvent);
        return response;
    }
}

