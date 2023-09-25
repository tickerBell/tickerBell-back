package com.tickerBell.domain.image.service;

import com.tickerBell.domain.image.dtos.ImageRequest;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<Image> uploadImage(MultipartFile multipartFile, List<MultipartFile> multipartFiles) throws IOException;

    List<ImageResponse> findAllImage();
    void deleteImage(List<Image> imageList);

}
