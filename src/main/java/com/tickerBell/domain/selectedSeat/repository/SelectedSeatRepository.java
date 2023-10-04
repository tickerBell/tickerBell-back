package com.tickerBell.domain.selectedSeat.repository;

import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedSeatRepository extends JpaRepository<SelectedSeat, Long> {
    @Query("select s from SelectedSeat s where s.ticketing.id = :ticketingId")
    List<SelectedSeat> findByTicketingId(@Param("ticketingId") Long ticketingId);
}
