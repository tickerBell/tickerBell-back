package com.tickerBell.domain.host.repository;

import com.tickerBell.domain.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {
    List<Host> findByEventId(@Param(value = "eventId") Long postId);
}
