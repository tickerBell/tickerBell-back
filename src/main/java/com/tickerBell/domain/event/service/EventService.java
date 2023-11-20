package com.tickerBell.domain.event.service;

import com.tickerBell.domain.event.dtos.*;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    Long saveEvent(Long memberId, SaveEventRequest request);
    EventCategoryResponse getEventByCategory(Category category, Pageable pageable);
    EventResponse findByIdFetchAll(Long eventId);
    MainPageDto getMainPage();
    Page<EventListResponse> findAllEvent(int page, int size);
    void cancelEventByEventId(Long eventId, Long memberId);

    //== graphql 에 사용 ==//
    List<EventListResponse> getEventByPlace(String place);
    List<EventListResponse> getEventByName(String name);

}
