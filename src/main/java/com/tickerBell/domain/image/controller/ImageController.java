package com.tickerBell.domain.image.controller;

import com.tickerBell.domain.image.dtos.EventImageResponse;
import com.tickerBell.domain.image.dtos.ImageRequest;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.domain.image.service.ImageS3Handler;
import com.tickerBell.domain.image.service.ImageService;
import com.tickerBell.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final ImageS3Handler imageS3Handler;

    @Operation(description = "이미지 등록 *")
    @PostMapping(value = "/api/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> uploadEventImage(@Parameter(description = "썸네일 이미지") @RequestPart(name = "thumbNailImage") MultipartFile thumbNailImage,
                                                     @Parameter(description = "이벤트 이미지") @RequestPart(name = "eventImages", required = false) List<MultipartFile> eventImages) {

        List<Image> imageList = imageS3Handler.uploadImage(thumbNailImage, eventImages);

        List<Image> savedImageList = imageService.saveImage(imageList);
        EventImageResponse eventImageResponse = EventImageResponse.from(savedImageList);

        return ResponseEntity.ok(new Response(eventImageResponse, "이미지 등록 완료"));
    }
}
