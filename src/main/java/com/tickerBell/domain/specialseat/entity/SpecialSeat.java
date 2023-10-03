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


    @Builder
    public SpecialSeat(Boolean isSpecialSeatA, Boolean isSpecialSeatB, Boolean isSpecialSeatC) {
        this.isSpecialSeatA = isSpecialSeatA;
        this.isSpecialSeatB = isSpecialSeatB;
        this.isSpecialSeatC = isSpecialSeatC;
    }
}
