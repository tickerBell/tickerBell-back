package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.dtos.MainPageDto;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EventRepositoryCustom {
    MainPageDto getMainPage();

    Page<Event> findByMemberIdFetchAllPage(Long memberId, Pageable pageable);
    Page<Event> findByCategoryFetchAllPage(Category category, Pageable pageable);
}
