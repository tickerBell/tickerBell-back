package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageS3HandlerTest {
    // 단위 테스트할 때 사용
    // @Mock 으로 만들어진 인스턴스들을 자동으로 주입
    @InjectMocks
    private ImageS3Handler imageS3Handler;

    // MockBean 스프링 컨텍스트에 등록함 -> 통합테스트에서 사용
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
        when(amazonS3Client.putObject(any())).thenReturn(any());

        // when
        List<Image> savedImageList = imageS3Handler.uploadImage(thumbnailImage, imageFiles);

        // then
        verify(amazonS3Client, times(3)).putObject(any(PutObjectRequest.class)); // s3 업로드 횟수 확인
        verify(amazonS3Client, times(3)).getUrl(any(), any()); // s3 조회 횟수 확인
    }

    @DisplayName("사진이 아닌 확장자 예외 처리")
    @Test
    public void testInvalidFileExtension() throws IOException {
        // given
        MultipartFile thumbnailImage = new MockMultipartFile("thumbnail.jpg", "thumbnail.jpg", "application/octet-stream", new byte[0]);
        List<MultipartFile> emptyMultipartFiles = new ArrayList<>();

        // when
        assertThatThrownBy(() -> imageS3Handler.uploadImage(thumbnailImage, emptyMultipartFiles))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.IMAGE_NOT_SUPPORTED_EXTENSION);

        // then
        verifyNoInteractions(amazonS3Client);
    }

    @DisplayName("확장자가 없는 파일 예외 처리")
    @Test
    public void testNullExtension() throws IOException {
        // given
        MultipartFile thumbnailImage = new MockMultipartFile("thumbnail.jpg", "thumbnail.jpg", "", new byte[0]);
        List<MultipartFile> emptyMultipartFiles = new ArrayList<>();

        // when
        assertThatThrownBy(() -> imageS3Handler.uploadImage(thumbnailImage, emptyMultipartFiles))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.IMAGE_NOT_FOUND_EXTENSION);

        // then
        verifyNoInteractions(amazonS3Client);
    }

    @Test
    @DisplayName("multipartFiles 이 null 일 때 테스트")
    void multipartNullTest() throws MalformedURLException {
        MultipartFile thumbnailImage = new MockMultipartFile("thumbnail.jpg", "thumbnail.jpg", "image/jpeg", new byte[0]);

        // stub
        when(amazonS3Client.getUrl(any(), any())).thenReturn(new URL("https://example-image.jpg"));
        when(amazonS3Client.putObject(any())).thenReturn(any());

        // when
        List<Image> savedImageList = imageS3Handler.uploadImage(thumbnailImage, null);

        // then
        verify(amazonS3Client, times(1)).putObject(any(PutObjectRequest.class)); // s3 업로드 횟수 확인
        verify(amazonS3Client, times(1)).getUrl(any(), any()); // s3 조회 횟수 확인
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
    void deleteImageTest() {
        // given
        List<String> storeImgList = new ArrayList<>();
        storeImgList.add("url1");

        // stub
        doNothing().when(amazonS3Client).deleteObject(any(DeleteObjectRequest.class));

        // when
        imageS3Handler.deleteImage(storeImgList);

        // then
        verify(amazonS3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}