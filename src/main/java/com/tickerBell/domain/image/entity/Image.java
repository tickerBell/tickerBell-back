package com.tickerBell.domain.image.entity;


import com.tickerBell.domain.common.BaseEntity;
import com.tickerBell.domain.event.entity.Event;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;
    private String originImgName; // 사용자가 올린 이미지 명
    private String storeImgName; // 저장된 사진 명
    private String s3Url; // s3 url 주소
    private Boolean isThumbnail; // 썸네일 여부
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public void setThumbnail(Boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    @Builder
    public Image(String originImgName, String storeImgName, String s3Url, Boolean isThumbnail, Event event) {
        this.originImgName = originImgName;
        this.storeImgName = storeImgName;
        this.s3Url = s3Url;
        this.isThumbnail = isThumbnail;
        addEvent(event);
    }

    // == 연관관계 메서드 == //
    public void addEvent(Event event) {
        this.event = event;
        event.getImages().add(this);
    }
}
