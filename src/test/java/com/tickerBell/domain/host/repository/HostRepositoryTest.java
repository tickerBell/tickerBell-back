package com.tickerBell.domain.host.repository;

import com.tickerBell.config.TestConfig;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.host.entity.Host;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class HostRepositoryTest {

    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private EventRepository eventRepository;

    @Test
    void saveTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);
        String hostName = "mockHost";
        Host host = Host.builder().hostName(hostName).event(savedEvent).build();

        // when
        Host savedHost = hostRepository.save(host);

        // then
        assertThat(savedHost).isEqualTo(host);
        assertThat(savedHost.getId()).isEqualTo(host.getId());
        assertThat(savedHost.getHostName()).isEqualTo(host.getHostName());
        assertThat(savedHost.getEvent()).isEqualTo(host.getEvent());
    }

    @Test
    void findByEventIdTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);
        String hostName = "mockHost";
        Host host = Host.builder().hostName(hostName).event(savedEvent).build();
        hostRepository.save(host);
        Host host2 = Host.builder().hostName(hostName).event(savedEvent).build();
        hostRepository.save(host2);

        // when
        List<Host> findHosts = hostRepository.findByEventId(savedEvent.getId());

        // then
        assertThat(findHosts.size()).isEqualTo(2);
    }
}