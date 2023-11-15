package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    @Query("select e from Event e where e.category = :category")
    List<Event> findByCategory(@Param("category") Category category);

    @Query("select e from Event e where e.startEvent >= :tomorrowStart and e.startEvent <= :tomorrowEnd")
    List<Event> findEventsTomorrow(@Param("tomorrowStart") LocalDateTime tomorrowStart, @Param("tomorrowEnd") LocalDateTime tomorrowEnd);

    @Query("select e from Event e join fetch e.specialSeat s where e.id = :eventId")
    List<Event> findByIdWithSpecialSeat(@Param("eventId") Long eventId);

    @Query(value = "select e from Event e" +
            " join fetch e.member em" +
            " join fetch e.specialSeat es" +
            " where e.id = :eventId")
    Event findByIdFetchAll(@Param("eventId") Long eventId);

    @Query(value = "select e from Event e" +
            " join fetch e.member em" +
            " join fetch e.specialSeat es" +
            " where e.member.id = :memberId")
    List<Event> findByMemberIdFetchAll(@Param("memberId") Long memberId);

    @Query("select e from Event e")
    Page<Event> findAllEventsPage(Pageable pageable);


    //== graphql 에서 사용 ==//
    List<Event> findByPlace(String place);
    List<Event> findByName(String name);

}
