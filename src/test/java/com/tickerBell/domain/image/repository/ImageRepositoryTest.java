package com.tickerBell.domain.image.repository;

import com.tickerBell.config.TestConfig;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.image.entity.Image;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 PK 로 썸네일 이미지 찾기 테스트")
    void findThumbNailImageByEventIdTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);
        Image image = Image.builder().isThumbnail(true).event(savedEvent).s3Url("s3Url").build();
        Image savedImage = imageRepository.save(image);

        // when
        Image findImage = imageRepository.findThumbNailImageByEventId(savedEvent.getId());

        // then
        Assertions.assertThat(findImage).isEqualTo(savedImage);
    }
}
