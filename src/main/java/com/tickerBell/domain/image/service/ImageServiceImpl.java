package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.image.dtos.ImageRequest;
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
import org.springframework.util.CollectionUtils;
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
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Override
    public List<Image> uploadImage(MultipartFile thumbNailImage, List<MultipartFile> multipartFiles) throws IOException {
        List<Image> imageList = new ArrayList<>();
        multipartFiles.add(thumbNailImage); // 일반 이미지 맨 뒤에 썸네일 이미지 추가

        if (!CollectionUtils.isEmpty(multipartFiles)) {

            for (MultipartFile multipartFile : multipartFiles) {
                String extension; //확장자명
                String contentType = multipartFile.getContentType();

                // dummy data 필터
                if (contentType.contains("application/octet-stream")) {
                    break;
                }
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
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

                InputStream inputStream = multipartFile.getInputStream();
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
        }
        // 맨 뒤 이미지 썸네일 처리
        imageList.get(imageList.size() - 1).setThumbnail(true);

        // todo: 임시로 저장. 나중에 event 와 연관관계 맺어야 함
        imageRepository.saveAll(imageList);
        return imageList;
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
        for (Image image : imageList) {
            String storeImageName = image.getStoreImgName();
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, storeImageName);
            amazonS3Client.deleteObject(deleteObjectRequest);
        }
        imageRepository.deleteAll(imageList);
        log.info("S3 파일 삭제 완료");
        log.info("DB 이미지 데이터 삭제 완료");
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
