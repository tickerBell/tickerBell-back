package com.tickerBell.domain.host.entity;

import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host {

    @Id @GeneratedValue
    @Column(name = "host_id")
    private Long id;

    private String hostName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event; // 호스트, 이벤트 N : 1 다대일 단방향 맵핑

    @Builder
    public Host(String hostName, Event event) {
        this.hostName = hostName;
        this.event = event;
    }
}
