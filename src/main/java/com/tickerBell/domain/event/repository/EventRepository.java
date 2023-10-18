package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e where e.category = :category")
    List<Event> findByCategory(@Param("category") Category category);

    @Query("select e from Event e where e.startEvent >= :tomorrowStart and e.startEvent <= :tomorrowEnd")
    List<Event> findEventsTomorrow(@Param("tomorrowStart") LocalDateTime tomorrowStart, @Param("tomorrowEnd") LocalDateTime tomorrowEnd);

    @Query("select e from Event e join fetch e.specialSeat s where e.id = :eventId")
    List<Event> findByIdWithSpecialSeat(@Param("eventId") Long eventId);
    
    @Query(value = "select e from Event e" +
            " join fetch e.member em" +
            " join fetch e.specialSeat es" +
            " join fetch e.castings ec" +
            " join fetch e.hosts eh" +
            " join fetch e.images ei" +
            " join fetch e.tags et" +
            " where e.id = :eventId")
    Event findByIdFetchAll(@Param("eventId") Long eventId);
}
