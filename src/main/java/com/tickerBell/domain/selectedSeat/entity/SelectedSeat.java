package com.tickerBell.domain.selectedSeat.entity;

import com.tickerBell.domain.ticketing.entity.Ticketing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
}
