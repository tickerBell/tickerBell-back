package com.tickerBell.domain.selectedSeat.entity;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedSeat {
    @Id
    @GeneratedValue
    @Column(name = "selected_seat_id")
    private Long id;

    private String seatInfo; // 선택 좌석
    private Float seatPrice; // 할인을 적용한 선택 좌석 가격
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketing_id")
    private Ticketing ticketing;

    @Builder
    public SelectedSeat(String seatInfo, Float seatPrice, Ticketing ticketing) {
        this.seatInfo = seatInfo;
        this.seatPrice = seatPrice;
        addTicketing(ticketing);
    }

    //== 연관관계 편의 메서드 ==//
    public void addTicketing(Ticketing ticketing) {
        this.ticketing = ticketing;
        ticketing.getSelectedSeatList().add(this);
    }
}
