package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    // 단위 테스트할 때 사용
    // @Mock 으로 만들어진 인스턴스들을 자동으로 주입
    @InjectMocks
    private ImageServiceImpl imageService;

    // MockBean 스프링 컨텍스트에 등록함 -> 통합테스트에서 사용
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private AmazonS3Client amazonS3Client;

    @DisplayName("이미지 업로드 및 조회")
    @Test
    public void testUploadImage() throws IOException {
        // given
        MultipartFile thumbnailImage = new MockMultipartFile("thumbnail.jpg", "thumbnail.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> imageFiles = new ArrayList<>();
        imageFiles.add(new MockMultipartFile("image1.jpg", "image1.jpg", "image/jpeg", new byte[0]));
        imageFiles.add(new MockMultipartFile("image2.png", "image2.png", "image/png", new byte[0]));

        // stub
        when(amazonS3Client.getUrl(any(), any())).thenReturn(new URL("https://example-image.jpg"));

        // when
        imageService.uploadImage(thumbnailImage, imageFiles);

        // then
        verify(amazonS3Client, times(3)).putObject(any(PutObjectRequest.class)); // s3 업로드 횟수 확인
        verify(amazonS3Client, times(3)).getUrl(any(), any()); // s3 조회 횟수 확인
    }

    @DisplayName("이미지 삭제")
    @Test
    public void testDeleteImage() throws IOException {
        // given
        List<Image> imageList = new ArrayList<>();
        Image image1 = Image.builder()
                .originImgName("image1.jpg")
                .build();
        Image image2 = Image.builder()
                .originImgName("image2.jpg")
                .build();
        imageList.add(image1);
        imageList.add(image2);

        // stub
        doNothing().when(amazonS3Client).deleteObject(any(DeleteObjectRequest.class));
        doNothing().when(imageRepository).deleteAll(any(List.class));

        // when
        imageService.deleteImage(imageList);

        // then
        verify(amazonS3Client, times(2)).deleteObject(any(DeleteObjectRequest.class)); // s3 삭제  횟수 확인
        verify(imageRepository, times(1)).deleteAll(any(List.class));
    }
}