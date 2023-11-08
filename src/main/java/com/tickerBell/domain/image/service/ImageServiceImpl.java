package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final EventRepository eventRepository;
    private final ImageS3Handler imageS3Handler;

    @Override
    public List<Image> saveImage(List<Image> imageList) {
        // 맨 뒤에 세팅해둔 썸네일 이미지 처리
        imageList.get(imageList.size() - 1).setThumbnail(true);

        // db에 이미지 저장
        List<Image> savedImageList = imageRepository.saveAll(imageList);

        return savedImageList;
    }

    // 테스트를 위해 임시로 만든 함수입니다.
    @Override
    public List<ImageResponse> findAllImage() {
        List<ImageResponse> imageResponseList = imageRepository.findAll().stream()
                .map(imageEntity -> ImageResponse.from(imageEntity))
                .collect(Collectors.toList());
        return imageResponseList;
    }

    @Override
    public void deleteImage(List<Image> imageList) {
        // todo: 이벤트 삭제 로직 작성 시 컨트롤러에서 이미지 먼저 삭제 후 s3에서 지워야 함
        // todo: eventId로 event 를 삭제 한다면 eventId에 해당하는 이미지를 DB 에서 조회해서 "컨트롤러에서" deleteImage 요청
        imageRepository.deleteAll(imageList);
        log.info("DB 이미지 데이터 삭제 완료");
    }

    @Override
    public List<Image> findByEventId(Long eventId) {
        return imageRepository.findImageByEventId(eventId);
    }

    @Override
    public void updateEventByImageUrl(String imageUrl, Long eventId) {
        Event findEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND)
        );

        Image findImage = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                () -> new CustomException(ErrorCode.IMAGE_NOT_FOUND)
        );
        findImage.updateEvent(findEvent);

    }

    private String createStoreImageName(String extension) {
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid + extension;
        return storeFileName;
    }
}
