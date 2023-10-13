package com.tickerBell.domain.host.repository;

import com.tickerBell.domain.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}
