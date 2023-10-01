package com.tickerBell.domain.tag.service;

public interface TagService {

    Long saveTag(String tagName, Long eventId, Long memberId);
}
