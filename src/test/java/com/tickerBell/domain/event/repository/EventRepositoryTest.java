package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.entity.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 저장 테스트")
    void saveTest() {
        // given
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        String casting = "mockCasting";
        Integer totalSeat = 60;
        Integer remainSeat = 60 ;
        String host = "mockHost";
        String place = "mockPlace";
        Event event = Event.builder()
                .name(name)
                .startEvent(startEvent)
                .endEvent(endEvent)
                .normalPrice(normalPrice)
                .premiumPrice(premiumPrice)
                .saleDegree(saleDegree)
                .casting(casting)
                .totalSeat(totalSeat)
                .remainSeat(remainSeat)
                .host(host)
                .place(place)
                .build();

        // when
        Event savedEvent = eventRepository.save(event);

        // then
        assertThat(savedEvent).isEqualTo(event);
        assertThat(savedEvent.getId()).isEqualTo(event.getId());
        assertThat(savedEvent.getName()).isEqualTo(event.getName());
        assertThat(savedEvent.getStartEvent()).isEqualTo(event.getStartEvent());
        assertThat(savedEvent.getEndEvent()).isEqualTo(event.getEndEvent());
        assertThat(savedEvent.getNormalPrice()).isEqualTo(event.getNormalPrice());
        assertThat(savedEvent.getPremiumPrice()).isEqualTo(event.getPremiumPrice());
        assertThat(savedEvent.getSaleDegree()).isEqualTo(event.getSaleDegree());
        assertThat(savedEvent.getCasting()).isEqualTo(event.getCasting());
        assertThat(savedEvent.getTotalSeat()).isEqualTo(event.getTotalSeat());
        assertThat(savedEvent.getRemainSeat()).isEqualTo(event.getRemainSeat());
        assertThat(savedEvent.getHost()).isEqualTo(event.getHost());
        assertThat(savedEvent.getPlace()).isEqualTo(event.getPlace());
    }
}