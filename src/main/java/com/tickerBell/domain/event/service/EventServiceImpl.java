package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
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

    private EventRepository eventRepository;

    public Long saveEvent(String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, Integer totalSeat, String host, String place) {
        Event event = Event.builder()
                .name(name)
                .startEvent(startEvent)
                .endEvent(endEvent)
                .normalPrice(normalPrice)
                .premiumPrice(premiumPrice)
                .saleDegree(saleDegree)
                .casting(casting)
                .totalSeat(totalSeat)
                .remainSeat(totalSeat) // remainSeat 는 등록 시 totalSeat 와 같다고 구현
                .host(host)
                .place(place)
                .build();

        Event savedEvent = eventRepository.save(event);
        return savedEvent.getId();
    }
}

