package com.tickerBell.domain.alarm.service;

import com.tickerBell.domain.alarm.dtos.SaveAlarmRequest;
import com.tickerBell.domain.alarm.repository.AlarmRepository;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmServiceImpl implements AlarmService{

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long saveAlarm(SaveAlarmRequest request) {
        Member findMember = memberRepository.findById(request.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        return null;
    }
}
