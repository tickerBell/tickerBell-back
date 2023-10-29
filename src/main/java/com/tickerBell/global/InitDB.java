package com.tickerBell.global;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.host.entity.Host;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.selectedSeat.entity.SelectedSeat;
import com.tickerBell.domain.specialseat.entity.SpecialSeat;
import com.tickerBell.domain.ticketing.entity.Ticketing;
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

            createEvent(member1, Category.CONCERT);
            createEvent(member1, Category.PLAY);
            createEvent(member1, Category.CLASSIC);
            createEvent(member1, Category.MUSICAL);
            createEvent(member1, Category.SPORTS);


//            SpecialSeat specialSeat = SpecialSeat.builder()
//                    .isSpecialSeatA(true)
//                    .isSpecialSeatB(true)
//                    .isSpecialSeatC(true)
//                    .build();
//            em.persist(specialSeat);
//
//
//            Event event = Event.builder()
//                    .name("이벤트1")
//                    .startEvent(LocalDateTime.now().plusDays(1))
//                    .endEvent(LocalDateTime.now().plusDays(1).plusHours(1))
//                    .normalPrice(10000)
//                    .premiumPrice(150000)
//                    .saleDegree(2000F)
//                    .totalSeat(60)
//                    .isAdult(true)
//                    .remainSeat(60)
//                    .place("장소1")
//                    .category(Category.CONCERT)
//                    .member(member1)
//                    .specialSeat(specialSeat)
//                    .build();
//            em.persist(event);
//
//            Casting casting = Casting.builder().event(event).castingName("공유").build();
//            em.persist(casting);
//
//            Host host = Host.builder().hostName("공유").event(event).build();
//            em.persist(host);
//
//            Image image = Image.builder().s3Url("s3url").storeImgName("storeImgName")
//                    .isThumbnail(true).originImgName("originalName").event(event).build();
//            em.persist(image);

        }

        private void createEvent(Member member1, Category category) {
            for (int i = 0; i < 10; i++) {
                SpecialSeat specialSeat = SpecialSeat.builder()
                        .isSpecialSeatA(true)
                        .isSpecialSeatB(true)
                        .isSpecialSeatC(true)
                        .build();
                em.persist(specialSeat);
                Event event = Event.builder()
                        .name(category.getDescription() + " 이벤트" + i)
                        .startEvent(LocalDateTime.now().plusDays(1).plusHours(1))
                        .endEvent(LocalDateTime.now().plusDays(1).plusHours(3))
                        .availablePurchaseTime(LocalDateTime.now().plusDays(1))
                        .normalPrice(10000 + (i * 5000))
                        .premiumPrice(15000 + + (i * 1000))
                        .saleDegree(2000F+ (i * 1000))
                        .totalSeat(60)
                        .isAdult(true)
                        .remainSeat(60)
                        .place("장소" + i)
                        .category(category)
                        .member(member1)
                        .specialSeat(specialSeat)
                        .build();
                em.persist(event);

                Casting casting = Casting.builder().event(event).castingName("배우" + i).build();
                em.persist(casting);

                Host host = Host.builder().hostName("주최자" + i).event(event).build();
                em.persist(host);

                Image image = Image.builder().s3Url("https://tickerbell-image.s3.ap-northeast-2.amazonaws.com/0f05a295-26e7-4082-a39a-1ddd0d05080d.png").storeImgName("storeImgName")
                        .isThumbnail(true).originImgName("originalName").event(event).build();
                em.persist(image);
            }
        }
    }
}
