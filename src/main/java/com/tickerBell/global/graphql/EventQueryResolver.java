package com.tickerBell.global.graphql;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.global.graphql.dtos.EventGraphqlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EventQueryResolver {
    private final EventService eventService;

    @QueryMapping
    public List<EventGraphqlResponse> getEventByPlace(@Argument String place) {
        log.info("getEventByPlace 호출");
        List<EventGraphqlResponse> eventList = eventService.getEventByPlace(place);

        return eventList;
    }

    @QueryMapping
    public List<EventGraphqlResponse> getEventByName(@Argument String name) {
        log.info("getEventByName 호출");
        List<EventGraphqlResponse> eventList = eventService.getEventByName(name);
        return eventList;
    }

    @QueryMapping
    public List<EventGraphqlResponse> getEventByCasting(@Argument String casting) {
        log.info("getEventByName 호출");
        List<EventGraphqlResponse> eventList = eventService.getEventByCasting(casting);
        return eventList;
    }

}
