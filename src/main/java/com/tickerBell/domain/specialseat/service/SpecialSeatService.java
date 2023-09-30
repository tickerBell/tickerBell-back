package com.tickerBell.domain.specialseat.service;

public interface SpecialSeatService {

    Long saveSpecialSeat(Long eventId, Boolean isSpecialSeatA, Boolean isSpecialSeatB, Boolean isSpecialSeatC);
}
