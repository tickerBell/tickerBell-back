package com.tickerBell.domain.alarm.service;

import com.tickerBell.domain.alarm.domain.Alarm;
import com.tickerBell.domain.alarm.dtos.SaveAlarmRequest;
import com.tickerBell.domain.alarm.repository.AlarmRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlarmServiceTest {

    @InjectMocks
    private AlarmServiceImpl alarmService;
    @Mock
    private AlarmRepository alarmRepository;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("알람 저장 테스트")
    void saveAlarmTest() {
        // given
        Long memberId = 1L;
        String message = "message";
        SaveAlarmRequest saveAlarmRequest = new SaveAlarmRequest();
        saveAlarmRequest.setMemberId(memberId);
        saveAlarmRequest.setMessage(message);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
        when(alarmRepository.save(any(Alarm.class))).thenReturn(Alarm.builder().build());

        // when
        alarmService.saveAlarm(saveAlarmRequest);

        // then
        verify(memberRepository, times(1)).findById(memberId);
        verify(alarmRepository, times(1)).save(any(Alarm.class));
    }
}
