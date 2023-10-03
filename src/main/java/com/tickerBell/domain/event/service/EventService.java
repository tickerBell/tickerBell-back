package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Long saveEvent(Long memberId, String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, String host, String place, Integer age, Category category);
    Long saveEvent2(Long memberId, SaveEventRequest request);
    List<EventListResponse> getEventByCategory(Category category);
}
