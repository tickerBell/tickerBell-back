package com.tickerBell.domain.image.service;

import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<Image> uploadImage(MultipartFile multipartFile, List<MultipartFile> multipartFiles);

    List<ImageResponse> findAllImage();
    void deleteImage(List<Image> imageList);
    List<Image> findByEventId(Long eventId);
    void updateEventByImageUrl(String imageUrl, Long eventId);

}
