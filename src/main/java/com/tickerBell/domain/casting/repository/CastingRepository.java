package com.tickerBell.domain.casting.repository;

import com.tickerBell.domain.casting.entity.Casting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastingRepository extends JpaRepository<Casting, Long> {
}
