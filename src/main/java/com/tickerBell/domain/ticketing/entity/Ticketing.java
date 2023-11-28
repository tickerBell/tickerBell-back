package com.tickerBell.domain.ticketing.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.NonMember;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticketing extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "ticketing_id")
    private Long id;

    private LocalDateTime selectedDate; // 예매 날짜

    private String paymentId; // 구매 식별 번호

    private Boolean isDelete; // 취소 여부

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
    public Ticketing(LocalDateTime selectedDate, String paymentId, Event event, Member member, NonMember nonMember) {
        this.selectedDate = selectedDate;
        this.paymentId = paymentId;
        this.isDelete = false;
        this.event = event;
        this.member = member;
        this.nonMember = nonMember;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}
