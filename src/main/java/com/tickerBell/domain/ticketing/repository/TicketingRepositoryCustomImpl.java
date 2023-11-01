package com.tickerBell.domain.ticketing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.tickerBell.domain.event.entity.QEvent.*;
import static com.tickerBell.domain.member.entity.QMember.member;
import static com.tickerBell.domain.ticketing.entity.QTicketing.ticketing;

@RequiredArgsConstructor
public class TicketingRepositoryCustomImpl implements TicketingRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Ticketing> findByMemberIdPage(Long memberId, Pageable pageable) {
        List<Ticketing> ticketings = queryFactory.select(ticketing)
                .from(ticketing)
                .join(ticketing.event, event).fetchJoin()
                .join(ticketing.member, member).fetchJoin()
                .where(ticketing.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(ticketing.count())
                .from(ticketing)
                .where(ticketing.member.id.eq(memberId))
                .fetchOne();

        return new PageImpl<>(ticketings, pageable, count);
    }
}
