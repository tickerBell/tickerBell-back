package com.tickerBell.domain.casting.repository;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CastingRepositoryTest {

    @Autowired
    private CastingRepository castingRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    void saveTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);
        String castingName = "mockCasting";
        Casting casting = Casting.builder().castingName(castingName).event(savedEvent).build();

        // when
        Casting savedCasting = castingRepository.save(casting);

        // then
        assertThat(savedCasting).isEqualTo(casting);
        assertThat(savedCasting.getId()).isEqualTo(casting.getId());
        assertThat(savedCasting.getCastingName()).isEqualTo(casting.getCastingName());
        assertThat(savedCasting.getEvent()).isEqualTo(casting.getEvent());
    }
}