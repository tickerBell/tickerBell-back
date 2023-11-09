package com.tickerBell.domain.selectedSeat.repository;


import com.tickerBell.config.TestConfig;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.specialseat.repository.SpecialSeatRepository;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
public class SelectedSeatRepositoryTest {
    @Autowired
    private SelectedSeatRepository selectedSeatRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketingRepository ticketingRepository;

    @Test
    @DisplayName("선택된 좌석 조회")
    public void findByEventIdAndSeatInfoTest() {
        // given
        Event event = Event.builder().build();
        Event savedEvent = eventRepository.save(event);
        Ticketing ticketing = Ticketing.builder().event(event).build();
        Ticketing savedTicketing = ticketingRepository.save(ticketing);
        String seatInfo = "A-1";
        SelectedSeat selectedSeat = SelectedSeat.builder().ticketing(savedTicketing).seatInfo(seatInfo).build();
        SelectedSeat savedSelectedSeat = selectedSeatRepository.save(selectedSeat);

        // when
        SelectedSeat findSelectedSeat = selectedSeatRepository.findByEventIdAndSeatInfo(savedEvent.getId(), seatInfo).get();

        // then
        assertThat(findSelectedSeat.getId()).isEqualTo(savedSelectedSeat.getId());
        assertThat(findSelectedSeat.getSeatInfo()).isEqualTo(savedSelectedSeat.getSeatInfo());
    }

}
