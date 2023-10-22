package com.tickerBell.domain.image.dtos;

import com.querydsl.core.annotations.QueryProjection;
import com.tickerBell.domain.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImageResponse {
    private Long imageId;
    private String s3Url; // s3 url 주소
    private String originImgName; // 사용자가 올린 이미지 명
    private Boolean isThumbnail; // 썸네일 여부

    // Image -> ImageResponse
    public static ImageResponse from(Image image) {
        return new ImageResponse(
        image.getId(),
        image.getS3Url(),
        image.getOriginImgName(),
        image.getIsThumbnail()
        );
    }

    @QueryProjection
    public ImageResponse(Long imageId, String s3Url, String originImgName, Boolean isThumbnail) {
        this.imageId = imageId;
        this.s3Url = s3Url;
        this.originImgName = originImgName;
        this.isThumbnail = isThumbnail;
    }
}
