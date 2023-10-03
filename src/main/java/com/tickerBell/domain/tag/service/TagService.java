package com.tickerBell.domain.tag.service;

import com.tickerBell.domain.tag.entity.Tag;

import java.util.List;

public interface TagService {

    Long saveTag(String tagName, Long eventId, Long memberId);
    Integer saveTagList(List<Tag> tagList);
}
