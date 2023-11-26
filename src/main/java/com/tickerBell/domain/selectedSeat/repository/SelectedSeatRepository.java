package com.tickerBell.domain.selectedSeat.repository;

import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelectedSeatRepository extends JpaRepository<SelectedSeat, Long> {
    @Query("select s from SelectedSeat s where s.ticketing.event.id = :eventId and s.seatInfo = :seatInfo")
    Optional<SelectedSeat> findByEventIdAndSeatInfo(@Param("eventId") Long eventId, @Param("seatInfo") String seatInfo);

    @Query("select s from SelectedSeat s where s.ticketing.event.id = :eventId")
    List<SelectedSeat> findByEventId(@Param("eventId") Long eventId);
}
