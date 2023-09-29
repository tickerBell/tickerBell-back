package com.tickerBell.domain.event.service;

import java.time.LocalDateTime;

public interface EventService {
    public Long saveEvent(String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, Integer totalSeat, String host, String place, Integer seatXBound, Integer seatYBound);
}
