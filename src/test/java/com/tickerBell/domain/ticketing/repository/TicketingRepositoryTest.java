package com.tickerBell.domain.ticketing.repository;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.selectedSeat.repository.SelectedSeatRepository;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class TicketingRepositoryTest {

    @Autowired
    private TicketingRepository ticketingRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SelectedSeatRepository selectedSeatRepository;

    @Test
    @DisplayName("이벤트 PK를 통한 티켓팅 조회 테스트")
    void findByEventIdTest() {
        // given
        Member registrantMember = Member.builder().build();
        Member savedRegistrantMember = memberRepository.save(registrantMember);
        Event event = Event.builder().member(savedRegistrantMember).build();
        Event savedEvent = eventRepository.save(event);
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        Ticketing ticketing = Ticketing.builder().event(savedEvent).member(savedMember).build();
        Ticketing savedTicketing = ticketingRepository.save(ticketing);
        SelectedSeat selectedSeat = SelectedSeat.builder().ticketing(savedTicketing).build();
        SelectedSeat savedSelectedSeat = selectedSeatRepository.save(selectedSeat);

        // when
        List<Ticketing> findTicketings = ticketingRepository.findByEventId(savedEvent.getId());

        // then
        assertThat(findTicketings.size()).isEqualTo(1);
        assertThat(findTicketings.get(0).getSelectedSeatList().size()).isEqualTo(1);
        assertThat(findTicketings.get(0).getSelectedSeatList().get(0).getTicketing()).isEqualTo(savedTicketing);
    }

}
