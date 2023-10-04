package com.tickerBell.domain.ticketing.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Ticketing extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "ticketing_id")
    private Long id;

    private String seatInfo; // 선택 좌석
    private Integer payment; // 지불 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
