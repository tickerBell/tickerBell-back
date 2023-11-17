package com.tickerBell.domain.event.repository;

import com.tickerBell.config.TestConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
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
        LocalDateTime availablePurchaseTime = LocalDateTime.now();
        Integer eventTime = 90;
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        Integer totalSeat = 60;
        Integer remainSeat = 60 ;
        String place = "mockPlace";
        Boolean isAdult = false;
        Category category = Category.MUSICAL;
        Event event = Event.builder()
                .name(name)
                .startEvent(startEvent)
                .endEvent(endEvent)
                .availablePurchaseTime(availablePurchaseTime)
                .eventTime(eventTime)
                .normalPrice(normalPrice)
                .premiumPrice(premiumPrice)
                .saleDegree(saleDegree)
                .totalSeat(totalSeat)
                .remainSeat(remainSeat)
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
        assertThat(savedEvent.getEventTime()).isEqualTo(event.getEventTime());
        assertThat(savedEvent.getNormalPrice()).isEqualTo(event.getNormalPrice());
        assertThat(savedEvent.getPremiumPrice()).isEqualTo(event.getPremiumPrice());
        assertThat(savedEvent.getSaleDegree()).isEqualTo(event.getSaleDegree());
        assertThat(savedEvent.getTotalSeat()).isEqualTo(event.getTotalSeat());
        assertThat(savedEvent.getRemainSeat()).isEqualTo(event.getRemainSeat());
        assertThat(savedEvent.getPlace()).isEqualTo(event.getPlace());
        assertThat(savedEvent.getIsAdult()).isEqualTo(event.getIsAdult());
        assertThat(savedEvent.getCategory()).isEqualTo(event.getCategory());
        assertThat(savedEvent.getCategory().name()).isEqualTo(event.getCategory().name());
        assertThat(savedEvent.getCategory().getDescription()).isEqualTo(event.getCategory().getDescription());
        assertThat(savedEvent.getMember()).isEqualTo(savedMember);
        assertThat(savedEvent.getSpecialSeat()).isEqualTo(savedSpecialSeat);
        assertThat(savedEvent.getViewCount()).isEqualTo(0);
        assertThat(savedEvent.getIsCancelled()).isFalse();
    }

    @Test
    @DisplayName("연관관계를 모두 조인한 이벤트 PK를 이용한 조회 테스트")
    void findByIdFetchAllTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);

        Event event = createMockEvent(savedMember, savedSpecialSeat);

        Event savedEvent = eventRepository.save(event);

        // when
        Event findEvents = eventRepository.findByIdFetchAll(savedEvent.getId());

        // then
        assertThat(findEvents).isEqualTo(savedEvent);
    }

    @Test
    @DisplayName("연관관계를 모두 조인한 회원 PK를 이용한 이벤트 조회 테스트")
    void findByMemberIdFetchAllTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);
        Event event = createMockEvent(savedMember, savedSpecialSeat);
        Event savedEvent = eventRepository.save(event);

        // when
        List<Event> findEvents = eventRepository.findByMemberIdFetchAll(savedMember.getId());

        // then
        assertThat(findEvents.size()).isEqualTo(1);
        assertThat(findEvents.get(0)).isEqualTo(savedEvent);
    }

    Event createMockEvent(Member member, SpecialSeat specialSeat) {
        String name = "mockName";
        LocalDateTime startEvent = LocalDateTime.now();
        LocalDateTime endEvent = LocalDateTime.now();
        Integer normalPrice = 100;
        Integer premiumPrice = 1000;
        Float saleDegree = 0.0F;
        Integer totalSeat = 60;
        Integer remainSeat = 60 ;
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
                .totalSeat(totalSeat)
                .remainSeat(remainSeat)
                .place(place)
                .isAdult(isAdult)
                .category(category)
                .member(member)
                .specialSeat(specialSeat)
                .build();

        return event;
    }

    @Test
    @DisplayName("페이징을 적용한 회원 PK를 통한 이벤트 조회 테스트")
    void findByMemberIdFetchAllPageTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);
        Event event = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event);
        Event event2 = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event2);
        Event event3 = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event3);

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Event> findEventsPage = eventRepository.findByMemberIdFetchAllPage(savedMember.getId(), pageRequest);
        List<Event> findEvents = findEventsPage.getContent();

        // then
        assertThat(findEvents.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("페이징을 적용한 카테고리로 이벤트 조회 테스트")
    void findByCategoryFetchAllPageTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder().isSpecialSeatC(true).isSpecialSeatB(true).isSpecialSeatC(true).build();
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);
        Event event = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event);
        Event event2 = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event2);
        Event event3 = createMockEvent(savedMember, savedSpecialSeat);
        eventRepository.save(event3);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Category category = Category.MUSICAL;

        // when
        Page<Event> findEventsPage = eventRepository.findByCategoryFetchAllPage(category, pageRequest);
        List<Event> findEvent = findEventsPage.getContent();

        // then
        assertThat(findEvent.size()).isEqualTo(3);
    }
}