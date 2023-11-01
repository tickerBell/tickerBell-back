package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketingRepositoryCustom {

    Page<Ticketing> findByMemberIdPage(Long memberId, Pageable pageable);
}
