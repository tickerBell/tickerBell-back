package com.tickerBell.domain.casting.entity;

import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Casting {

    @Id
    @GeneratedValue
    @Column(name = "casting_id")
    private Long id;

    private String castingName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event; // 출연자, 이벤트 N : 1 다대일 단방향 맵핑

    @Builder
    public Casting(String castingName, Event event) {
        this.castingName = castingName;
        this.event = event;
    }
}
