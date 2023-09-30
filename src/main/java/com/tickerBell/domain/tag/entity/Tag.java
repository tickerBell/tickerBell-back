package com.tickerBell.domain.tag.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String tagName; // 태그 명
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Tag(String tagName, Event event, Member member) {
        this.tagName = tagName;
        this.event = event;
        this.member = member;
    }
}
