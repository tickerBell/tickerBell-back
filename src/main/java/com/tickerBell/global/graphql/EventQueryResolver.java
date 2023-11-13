//package com.tickerBell.global.graphql;
//
//import com.tickerBell.domain.event.entity.Event;
//import com.tickerBell.domain.event.service.EventService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class EventQueryResolver {
//    private final EventService eventService;
//
//    @QueryMapping
//    public List<Event> getEventByPlace(String place) {
//        return eventService.getEventByPlace(place);
//    }
//
//    @QueryMapping
//    public List<Event> getEventByName(String name) {
//        return eventService.getEventByName(name);
//    }
//
//}
