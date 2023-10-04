package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long> {

    @Query("select t from Ticketing t join fetch t.member m where t.event.startEvent between :tomorrowStart and :tomorrowEnd")
    List<Ticketing> findTicketingTomorrow(@Param("tomorrowStart") LocalDateTime tomorrowStart, @Param("tomorrowEnd") LocalDateTime tomorrowEnd);

    @Query("select t from Ticketing t join fetch t.event e where t.member.id = :memberId")
    List<Ticketing> findByMemberId(@Param("memberId") Long memberId);
}
