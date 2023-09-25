package com.tickerBell.domain.category.entity;

import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name; // 카테고리 명

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
