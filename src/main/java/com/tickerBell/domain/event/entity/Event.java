package com.tickerBell.domain.event.entity;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    private String name; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private LocalDateTime endEvent; // 이벤트 종료 시간
    private LocalTime dailyStartEvent; // 하루 중 시작 시간
    private LocalDateTime availablePurchaseTime; // 구매 가능 시간
    private Integer eventTime; // 이벤트 시간
    private Integer normalPrice; // 일반 좌석 가격
    private Integer premiumPrice; // 앞열 좌석 가격
    private Float saleDegree; // 1.0 이상: n 원 할인  |  1.0 미만: n 퍼센트 할인 | 0: 세일 x
    private Integer totalSeat; // 전체 좌석 수
    private Integer remainSeat; // 남은 좌석 수
    private String place; // 주소
    private String description; // 이벤트 설명
    private Boolean isAdult; // 제한연령
    private Integer viewCount; // 조회수
    private Boolean isCancelled; // 취소 여부
    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 이벤트, 회원 N : 1 다대일 단방향 맵핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "special_seat_id")
    private SpecialSeat specialSeat;
    @OneToMany(mappedBy = "event")
    private List<Casting> castingList = new ArrayList<>();
    @OneToMany(mappedBy = "event")
    private List<Image> imageList = new ArrayList<>();
    @OneToMany(mappedBy = "event")
    private List<Host> hostList = new ArrayList<>();
    @OneToMany(mappedBy = "event")
    private List<Ticketing> ticketingList = new ArrayList<>(); // ticketing 양방향 연관 관계 추가

    @Builder
    public Event(String name, LocalDateTime startEvent, LocalTime dailyStartEvent, LocalDateTime endEvent, LocalDateTime availablePurchaseTime, Integer eventTime, Integer normalPrice, Integer premiumPrice, Float saleDegree, Integer totalSeat, Integer remainSeat, String place, String description, Boolean isAdult, Category category, Member member, SpecialSeat specialSeat) {
        this.name = name;
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.dailyStartEvent = dailyStartEvent;
        this.eventTime = eventTime;
        this.availablePurchaseTime = availablePurchaseTime;
        this.normalPrice = normalPrice;
        this.premiumPrice = premiumPrice;
        this.saleDegree = saleDegree;
        this.totalSeat = totalSeat;
        this.remainSeat = remainSeat;
        this.place = place;
        this.description = description;
        this.isAdult = isAdult;
        this.category = category;
        this.member = member;
        this.specialSeat = specialSeat;
        this.viewCount = 0;
        this.isCancelled = false;
    }

    public void setRemainSeat(Integer remainSeat) {
        this.remainSeat = remainSeat;
    }
    public void updateViewCount() {
        this.viewCount += 1;
    }
    public void cancelEvent() {
        this.isCancelled = true;
    }
}
