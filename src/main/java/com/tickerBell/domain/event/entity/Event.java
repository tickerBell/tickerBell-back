package com.tickerBell.domain.event.entity;

import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    private String name; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private LocalDateTime endEvent; // 이벤트 종료 시간
    private LocalDateTime availablePurchaseTime; // 구매 가능 시간
    private Integer normalPrice; // 일반 좌석 가격
    private Integer premiumPrice; // 앞열 좌석 가격
    private Float saleDegree; // 1.0 이상: n 원 할인  |  1.0 미만: n 퍼센트 할인 | 0: 세일 x
    private Integer totalSeat; // 전체 좌석 수
    private Integer remainSeat; // 남은 좌석 수
    private String place; // 주소
    private Boolean isAdult; // 제한연령
    private Integer viewCount = 0; // 조회수
    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 이벤트, 회원 N : 1 다대일 단방향 맵핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "special_seat_id")
    private SpecialSeat specialSeat;

    @Builder
    public Event(String name, LocalDateTime startEvent, LocalDateTime endEvent, LocalDateTime availablePurchaseTime, Integer normalPrice, Integer premiumPrice, Float saleDegree, Integer totalSeat, Integer remainSeat, String place, Boolean isAdult, Category category, Member member, SpecialSeat specialSeat, Integer viewCount) {
        this.name = name;
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.availablePurchaseTime = availablePurchaseTime;
        this.normalPrice = normalPrice;
        this.premiumPrice = premiumPrice;
        this.saleDegree = saleDegree;
        this.totalSeat = totalSeat;
        this.remainSeat = remainSeat;
        this.place = place;
        this.isAdult = isAdult;
        this.category = category;
        this.member = member;
        this.specialSeat = specialSeat;
        this.viewCount = viewCount;
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }
}
