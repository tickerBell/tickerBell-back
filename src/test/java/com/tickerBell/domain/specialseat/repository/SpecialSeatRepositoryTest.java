package com.tickerBell.domain.specialseat.repository;

import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SpecialSeatRepositoryTest {

    @Autowired
    private SpecialSeatRepository specialSeatRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("특수석 저장 테스트")
    void saveTest() {
        // given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);
        SpecialSeat specialSeat = SpecialSeat.builder()
                .isSpecialSeatA(true)
                .isSpecialSeatB(true)
                .isSpecialSeatC(true)
                .build();

        // when
        SpecialSeat savedSpecialSeat = specialSeatRepository.save(specialSeat);

        // then
        assertThat(savedSpecialSeat).isEqualTo(specialSeat);
        assertThat(savedSpecialSeat.getId()).isEqualTo(specialSeat.getId());
        assertThat(savedSpecialSeat.getIsSpecialSeatA()).isEqualTo(specialSeat.getIsSpecialSeatA());
        assertThat(savedSpecialSeat.getIsSpecialSeatB()).isEqualTo(specialSeat.getIsSpecialSeatB());
        assertThat(savedSpecialSeat.getIsSpecialSeatC()).isEqualTo(specialSeat.getIsSpecialSeatC());
    }
}