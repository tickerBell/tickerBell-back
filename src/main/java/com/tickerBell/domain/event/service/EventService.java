package com.tickerBell.domain.event.service;

import java.time.LocalDateTime;

public interface EventService {
    Long saveEvent(String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, String host, String place);
}
