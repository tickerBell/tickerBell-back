package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageS3Handler {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Image> uploadImage(MultipartFile thumbNailImage, List<MultipartFile> multipartFiles) {
        List<Image> imageList = new ArrayList<>();
        // 일반 이미지 맨 뒤에 썸네일 이미지 추가
        // 썸네일 이미지는 필수 값으로 받아 옴
        multipartFiles.add(thumbNailImage);


        for (MultipartFile multipartFile : multipartFiles) {
            String extension; //확장자명
            String contentType = multipartFile.getContentType();

            if (ObjectUtils.isEmpty(contentType)) {
                throw new CustomException(ErrorCode.IMAGE_NOT_FOUND_EXTENSION);
            } else { //확장자명이 jpeg, png 인 파일들만 받아서 처리
                if (contentType.contains("image/jpeg")) extension = ".jpg";
                else if (contentType.contains("image/png")) extension = ".png";
                else {
                    log.info("사진이 아닌 파일입니다.");
                    throw new CustomException(ErrorCode.IMAGE_NOT_SUPPORTED_EXTENSION);
                }
            }

            // unique 이름 생성
            String storeImageName = createStoreImageName(extension);

            InputStream inputStream = null;
            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                throw new CustomException(ErrorCode.IMAGE_EXCEPTION);
            }
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());

            PutObjectRequest request = new PutObjectRequest(bucket, storeImageName, inputStream, metadata);

            // s3 put
            amazonS3Client.putObject(request);
            log.info("s3에 사진 저장");

            // s3 get
            String s3Url = getImgPath(storeImageName);

            Image image = Image.builder()
                    .originImgName(multipartFile.getOriginalFilename())
                    .storeImgName(storeImageName)
                    .s3Url(s3Url)
                    .isThumbnail(false)
                    .build();

            imageList.add(image);
        }
        return imageList;
    }

    public void deleteImage(List<String> storeImgList) {
        // todo: 이미지 삭제
        for (String storeImgName : storeImgList) {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImgName);
            amazonS3Client.deleteObject(deleteObjectRequest);
        }
        log.info("S3 파일 삭제 완료");
    }

    private String createStoreImageName(String extension) {
        String uuid = UUID.randomUUID().toString();
        String storeFileName = uuid + extension;
        return storeFileName;
    }

    public String getImgPath(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
