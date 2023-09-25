package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long> {
}
