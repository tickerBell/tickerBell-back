package com.tickerBell.domain.event.repository;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.specialseat.repository.SpecialSeatRepository;
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SpecialSeatRepository specialSeatRepository;

    @Test
    @DisplayName("이벤트 저장 테스트")
    void saveTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);

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
        Boolean isAdult = false;
        Category category = Category.MUSICAL;
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
                .isAdult(isAdult)
                .category(category)
                .member(member)
                .specialSeat(specialSeat)
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
        assertThat(savedEvent.getIsAdult()).isEqualTo(event.getIsAdult());
        assertThat(savedEvent.getCategory()).isEqualTo(event.getCategory());
        assertThat(savedEvent.getCategory().name()).isEqualTo(event.getCategory().name());
        assertThat(savedEvent.getCategory().getDescription()).isEqualTo(event.getCategory().getDescription());
        assertThat(savedEvent.getMember()).isEqualTo(savedMember);
        assertThat(savedEvent.getSpecialSeat()).isEqualTo(savedSpecialSeat);
    }

    @Test
    @DisplayName("연관관계를 모두 조인한 이벤트 PK를 이용한 조회 테스트")
    void findByIdFetchAllTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);

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
        Boolean isAdult = true;
        Category category = Category.MUSICAL;
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
                .isAdult(isAdult)
                .category(category)
                .member(member)
                .specialSeat(savedSpecialSeat)
                .build();
        Event savedEvent = eventRepository.save(event);

        // when
        Event findEvents = eventRepository.findByIdFetchAll(savedEvent.getId());

        // then
        assertThat(findEvents).isEqualTo(savedEvent);
    }
}