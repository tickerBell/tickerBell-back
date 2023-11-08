package com.tickerBell.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.image.dtos.ImageResponse;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import org.assertj.core.api.AbstractObjectAssert;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    @Mock
    private EventRepository eventRepository;

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
        doNothing().when(imageRepository).deleteAll(any(List.class));

        // when
        imageService.deleteImage(imageList);

        // then
        verify(imageRepository, times(1)).deleteAll(any(List.class));
    }

    @Test
    @DisplayName("이벤트 PK로 조회 테스트")
    void findByEventIdTest() {
        // given
        Long eventId = 1L;
        List<Image> imageList = new ArrayList<>();
        imageList.add(Image.builder().build());

        // stub
        when(imageRepository.findImageByEventId(eventId)).thenReturn(imageList);

        // when
        List<Image> findImages = imageService.findByEventId(eventId);

        // then
        assertThat(findImages).isNotNull();
        assertThat(findImages.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트 연관관계 업데이트 테스트")
    void updateEventByImageUrlTest() {
        // given
        String imageUrl = "url";
        Long eventId = 1L;
        Event event = Event.builder().build();
        Image image = Image.builder().build();

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(imageRepository.findByImageUrl(imageUrl)).thenReturn(Optional.of(image));

        // when
        imageService.updateEventByImageUrl(imageUrl, eventId);

        // then
        verify(eventRepository, times(1)).findById(eventId);
        verify(imageRepository, times(1)).findByImageUrl(imageUrl);
        assertThat(image.getEvent()).isEqualTo(event);
    }

    @Test
    @DisplayName("이벤트 연관관계 업데이트 이벤트 조회 실패 테스트")
    void updateEventByImageUrlEventFailTest() {
        // given
        String imageUrl = "url";
        Long eventId = 1L;
        Image image = Image.builder().build();

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(
                () -> imageService.updateEventByImageUrl(imageUrl, eventId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EVENT_NOT_FOUND);
        });
        verify(eventRepository, times(1)).findById(eventId);
        verifyNoInteractions(imageRepository);
        assertThat(image.getEvent()).isNull();
    }
    @Test
    @DisplayName("이벤트 연관관계 업데이트 이미지 조회 실패 테스트")
    void updateEventByImageUrlImageFailTest() {
        // given
        String imageUrl = "url";
        Long eventId = 1L;
        Event event = Event.builder().build();
        Image image = Image.builder().build();

        // stub
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(imageRepository.findByImageUrl(imageUrl)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(
                () -> imageService.updateEventByImageUrl(imageUrl, eventId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.IMAGE_NOT_FOUND);
        });
        verify(eventRepository, times(1)).findById(eventId);
        verify(imageRepository, times(1)).findByImageUrl(imageUrl);
        assertThat(image.getEvent()).isNull();
    }
}