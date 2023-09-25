package com.tickerBell.domain.image.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Image extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    private String storeImgName; // 저장된 사진 명
    private String s3Url; // s3 url 주소
    private Boolean isThumbnail; // 썸네일 여부
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
