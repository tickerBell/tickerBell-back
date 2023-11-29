package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long>, TicketingRepositoryCustom {

    @Query("select t from Ticketing t join fetch t.member m where t.event.startEvent between :tomorrowStart and :tomorrowEnd")
    List<Ticketing> findTicketingTomorrow(@Param("tomorrowStart") LocalDateTime tomorrowStart, @Param("tomorrowEnd") LocalDateTime tomorrowEnd);

    @Query("select t from Ticketing t where t.member.id = :memberId")
    Page<Ticketing> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select t from Ticketing t where t.nonMember.id = :nonMemberId")
    Page<Ticketing> findByNonMemberId(@Param("nonMemberId") Long nonMemberId, Pageable pageable);

    @Query("select t from Ticketing t join fetch t.event e where t.id = :ticketingId")
    Optional<Ticketing> findByIdWithEvent(@Param("ticketingId") Long ticketingId);
    @Query("select t from Ticketing t join fetch t.event e " +
            "where t.id = :ticketingId and t.nonMember.name = :name and t.nonMember.phone = :phone")
    Optional<Ticketing> findByIdAndNonMemberWithEvent(@Param("ticketingId") Long ticketingId,
                                                      @Param("name") String name, @Param("phone") String phone);

    @Query("select t from Ticketing t" +
            " join fetch t.event" +
            " join fetch t.member" +
            " join fetch t.selectedSeatList ts" +
            " where t.event.id = :eventId")
    List<Ticketing> findByEventId(@Param("eventId") Long eventId);
}
