package com.tickerBell.domain.specialseat.entity;

import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialSeat {

    @Id
    @GeneratedValue
    @Column(name = "special_seat_id")
    private Long id;

    private Boolean isSpecialSeatA; // A 좌석 특수석인가
    private Boolean isSpecialSeatB; // B 좌석이 특수석인가
    private Boolean isSpecialSeatC; // C 좌석이 특수석인가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event; // 이벤트, 특수석 N : 1 다대일 단방향 맵핑

    @Builder
    public SpecialSeat(Boolean isSpecialSeatA, Boolean isSpecialSeatB, Boolean isSpecialSeatC, Event event) {
        this.isSpecialSeatA = isSpecialSeatA;
        this.isSpecialSeatB = isSpecialSeatB;
        this.isSpecialSeatC = isSpecialSeatC;
        this.event = event;
    }
}
