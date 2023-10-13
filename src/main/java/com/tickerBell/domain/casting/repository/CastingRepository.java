package com.tickerBell.domain.casting.repository;

import com.tickerBell.domain.casting.entity.Casting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastingRepository extends JpaRepository<Casting, Long> {

    List<Casting> findByEventId(@Param(value = "eventId") Long eventId);
}
