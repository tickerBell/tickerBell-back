package com.tickerBell.global.batch;

import com.tickerBell.domain.emitter.service.EmitterService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestScheduled {

    private final EmitterService emitterService;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0/5 0-1 * * ?")
    public void testSchedule() {
        List<Member> findMembers = memberRepository.findAll();

        for (Member findMember : findMembers) {
            String message = makeMessage("이벤트가", 1L, 10L);
            emitterService.notify(findMember.getId(), message);
        }
    }

    @Scheduled(cron = "0 0/3 15-17 * * ?")
    public void selfTestSchedule() {
        List<Member> findMembers = memberRepository.findAll();

        for (Member findMember : findMembers) {
            String message = makeMessage("이벤트가", 1L, 10L);
            emitterService.notify(findMember.getId(), message);
        }
    }


    private String makeMessage(String name, Long hour, Long minute) {
        return "예매하신 " + name + "가 "
                + hour + " 시간 "
                + minute + " 분 "
                + "남았습니다.";
    }
}
