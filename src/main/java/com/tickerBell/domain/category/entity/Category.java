package com.tickerBell.domain.category.entity;

import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Categories categories; // 카테고리 명

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    public Category(Categories categories, Event event) {
        this.categories = categories;
        this.event = event;
    }
}
