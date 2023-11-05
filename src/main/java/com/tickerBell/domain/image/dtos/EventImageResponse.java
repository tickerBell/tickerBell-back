package com.tickerBell.domain.image.dtos;

import com.tickerBell.domain.image.entity.Image;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class EventImageResponse {
    private String thumbNailImageUrl;
    private List<String> imageUrls;

    public static EventImageResponse from(List<Image> images) {

        EventImageResponseBuilder builder = EventImageResponse.builder();
        List<String> urls = new ArrayList<>();

        for (Image image : images) {
            if (image.getIsThumbnail()) {
                builder.thumbNailImageUrl(image.getS3Url());
            } else {
                urls.add(image.getS3Url());
            }
        }
        return builder.imageUrls(urls).build();
    }
}
