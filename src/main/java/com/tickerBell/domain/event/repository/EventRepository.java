package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e where e.category.id = :categoryId")
    List<Event> findByCategoryName(@Param("categoryId") Long categoryId);
}
