package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.*;
import com.tickerBell.domain.event.entity.Category;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Long saveEvent(Long memberId, SaveEventRequest request);
    EventCategoryResponse getEventByCategory(Category category, Pageable pageable);
    EventResponse findByIdFetchAll(Long eventId);
    MainPageDto getMainPage();
}
