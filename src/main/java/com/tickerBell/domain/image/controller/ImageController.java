package com.tickerBell.domain.image.controller;

import com.tickerBell.domain.image.dtos.ImageRequest;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.domain.image.service.ImageService;
import com.tickerBell.global.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadImage(@Valid @ModelAttribute ImageRequest imageRequest) throws IOException {

        List<Image> imageList = imageService.uploadImage(imageRequest.getThumbNailImg(), imageRequest.getImageList());
        return ResponseEntity.ok(new Response(imageList, "이미지 업로드 완료"));
    }

    @GetMapping("/image")
    public ResponseEntity<Response> getAllImage() {
        List<ImageResponse> imageResponseList = imageService.findAllImage();
        return ResponseEntity.ok(new Response(imageResponseList, "전체 이미지 반환 완료"));
    }

    @DeleteMapping("/image")
    public ResponseEntity<Response> deleteAllImage() {
        imageService.deleteImage(imageRepository.findAll());
        return ResponseEntity.ok(new Response("전체 이미지 삭제 완료"));
    }
}
