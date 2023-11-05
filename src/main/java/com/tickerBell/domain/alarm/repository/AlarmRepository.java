package com.tickerBell.domain.alarm.repository;

import com.tickerBell.domain.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query(value = "select a from Alarm a left join fetch a.member" +
            " where a.member.id = :memberId" +
            " order by a.createdDate asc",
            countQuery = "select a from Alarm a" +
                    " where a.member.id = :memberId" +
                    " order by a.createdDate asc")
    List<Alarm> findAllByMemberIdFetch(@Param("memberId") Long memberId);
}
