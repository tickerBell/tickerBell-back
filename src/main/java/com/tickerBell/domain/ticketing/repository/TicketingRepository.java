package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketingRepository extends JpaRepository<Ticketing, Long> {

    @Query("select t from Ticketing t join fetch t.member m where t.event.startEvent between :tomorrowStart and :tomorrowEnd")
    List<Ticketing> findTicketingTomorrow(@Param("tomorrowStart") LocalDateTime tomorrowStart, @Param("tomorrowEnd") LocalDateTime tomorrowEnd);

    @Query("select t from Ticketing t join fetch t.event e where t.member.id = :memberId")
    List<Ticketing> findByMemberId(@Param("memberId") Long memberId);

    @Query("select t from Ticketing t join fetch t.event e where t.nonMember.id = :nonMemberId")
    List<Ticketing> findByNonMemberId(@Param("nonMemberId") Long nonMemberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Ticketing t where t.member.id = :memberId and t.id = :ticketingId")
    void deleteByMemberAndTicketing(@Param("memberId") Long memberId, @Param("ticketingId") Long ticketingId);

    @Query("select t from Ticketing t join fetch t.event e where t.id = :ticketingId")
    Optional<Ticketing> findByIdWithEvent(@Param("ticketingId") Long ticketingId);
    @Query("delete from Ticketing t where t.nonMember.id = :nonMemberId")
    void deleteByNonMember(@Param("nonMemberId") Long nonMemberId);
}
