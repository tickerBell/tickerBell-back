package com.tickerBell.domain.alarm.repository;

import com.tickerBell.config.TestConfig;
import com.tickerBell.domain.alarm.domain.Alarm;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class AlarmRepositoryTest {

    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("알람 저장 테스트")
    void saveTest() {
        // given
        Member savedMember = memberRepository.save(Member.builder().build());
        String message = "message";
        Boolean isRead = false;
        Alarm alarm = Alarm.builder().message(message).isRead(isRead).member(savedMember).build();

        // when
        Alarm savedAlarm = alarmRepository.save(alarm);

        // then
        assertThat(savedAlarm).isEqualTo(alarm);
        assertThat(savedAlarm.getId()).isEqualTo(alarm.getId());
        assertThat(savedAlarm.getMessage()).isEqualTo(alarm.getMessage());
        assertThat(savedAlarm.getIsRead()).isEqualTo(alarm.getIsRead());
        assertThat(savedAlarm.getMember()).isEqualTo(alarm.getMember());
    }
}
