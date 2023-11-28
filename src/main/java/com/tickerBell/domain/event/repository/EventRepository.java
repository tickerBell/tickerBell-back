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
import java.util.Optional;

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

    @Query("select e from Event e join e.imageList i on i.isThumbnail = true " +
            "where e.category = :category and e.startEvent > :startEvent " +
            "order by e.viewCount DESC")
    List<Event> findByCategoryInMainPage(@Param("category") Category category,
                                        @Param("startEvent") LocalDateTime startEvent,
                                        Pageable pageable);

    @Query("select e from Event e join e.imageList i on i.isThumbnail = true " +
            "where e.saleDegree != 0 and e.startEvent > :startEvent " +
            "order by e.startEvent asc")
    List<Event> findBySaleInMainPage(@Param("startEvent") LocalDateTime now, Pageable pageable);

    @Query("select e from Event e join e.imageList i on i.isThumbnail = true " +
            "where e.availablePurchaseTime > :startEvent " +
            "order by e.availablePurchaseTime asc")
    List<Event> findByDeadLineInMainPage(@Param("startEvent") LocalDateTime now, Pageable pageable);

    @Query("select case when count(e) > 0 then true else false end from Event e " +
            "where e.id = :eventId and :selectedDate between e.startEvent and e.endEvent")
    Boolean validSelectedDate(@Param("eventId") Long eventId, @Param("selectedDate") LocalDateTime selectedDate);



    //== graphql 에서 사용 ==//
    @Query("select e from Event e where e.place like %:place%")
    List<Event> findByPlace(@Param("place") String place);
    @Query("select e from Event e where e.name like %:name%")
    List<Event> findByName(@Param("name") String name);
    @Query("select distinct e from Casting c join c.event e where c.castingName like %:casting%")
    List<Event> findByCasting(@Param("casting") String casting);

}
