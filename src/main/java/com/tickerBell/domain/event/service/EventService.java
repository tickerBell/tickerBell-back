package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.EventListResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Long saveEvent(Long memberId, String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, String host, String place, Integer age, String categoryName);
    List<EventListResponse> getEventByCategory(String categoryName);
}
