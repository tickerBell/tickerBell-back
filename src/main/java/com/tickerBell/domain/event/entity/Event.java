package com.tickerBell.domain.event.entity;

import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    private String name; // 이벤트 이름
    private LocalDateTime startEvent; // 이벤트 시작 시간
    private LocalDateTime endEvent; // 이벤트 시작 시간
    private Integer normalPrice; // 일반 좌석 가격
    private Integer premiumPrice; // 앞열 좌석 가격
    private Float saleDegree; // 1.0 이상: n 원 할인  |  1.0 미만: n 퍼센트 할인
    @Column(columnDefinition = "TEXT")
    private String casting; // 출연진 정보 (, 로 구분해서 저장)
    private Integer totalSeat; // 전체 좌석 수
    private Integer remainSeat; // 남은 좌석 수
    private String host; // 주최자, 스폰서 (, 로 구분해서 저장)
    private String place; // 주소
    private Integer age; // 제한연령
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 이벤트, 회원 N : 1 다대일 단방향 맵핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Event(String name, LocalDateTime startEvent, LocalDateTime endEvent, Integer normalPrice, Integer premiumPrice, Float saleDegree, String casting, Integer totalSeat, Integer remainSeat, String host, String place, Integer age, Member member) {
        this.name = name;
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.normalPrice = normalPrice;
        this.premiumPrice = premiumPrice;
        this.saleDegree = saleDegree;
        this.casting = casting;
        this.totalSeat = totalSeat;
        this.remainSeat = remainSeat;
        this.host = host;
        this.place = place;
        this.age = age;
        this.member = member;
    }
}
