package com.tickerBell.domain.ticketing.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.NonMember;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticketing extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "ticketing_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 양방향 매핑
    @OneToMany(mappedBy = "ticketing", cascade = CascadeType.REMOVE)
    private List<SelectedSeat> selectedSeatList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_member_id")
    private NonMember nonMember;

    @Builder
    public Ticketing(Event event, Member member, NonMember nonMember) {
        this.event = event;
        this.member = member;
        this.nonMember = nonMember;
    }
}
