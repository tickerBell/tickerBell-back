package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.EventResponse;
import com.tickerBell.domain.event.dtos.MainPageDto;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;

import java.util.List;

public interface EventService {
    Long saveEvent(Long memberId, SaveEventRequest request);
    List<EventListResponse> getEventByCategory(Category category);
    EventResponse findByIdFetchAll(Long eventId);
    MainPageDto getMainPage();
}
