package com.tickerBell.global;

import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final InitService initService;

    public static long time;

    @PostConstruct
    public void init() {
        initService.initMember();
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class InitService {
        private final EntityManager em;
        private final BCryptPasswordEncoder encoder;

        public void initMember() {
            Member member1 = Member.builder().username("abcdefg").password(encoder.encode("abcdefg1")).phone("01031725949").isAdult(true)
                    .role(Role.ROLE_REGISTRANT).authProvider(AuthProvider.KAKAO).build();
            em.persist(member1);

            SpecialSeat specialSeat = SpecialSeat.builder()
                    .isSpecialSeatA(true)
                    .isSpecialSeatB(true)
                    .isSpecialSeatC(true)
                    .build();
            em.persist(specialSeat);

            Event event = Event.builder()
                    .name("이벤트1")
                    .startEvent(LocalDateTime.now().plusDays(1))
                    .endEvent(LocalDateTime.now().plusDays(2))
                    .normalPrice(10000)
                    .premiumPrice(150000)
                    .saleDegree(2000F)
                    .casting("출연자1")
                    .totalSeat(60)
                    .host("호스트1")
                    .age(20)
                    .remainSeat(60)
                    .place("장소1")
                    .category(Category.CONCERT)
                    .member(member1)
                    .specialSeat(specialSeat)
                    .build();

            em.persist(event);
        }
    }
}
