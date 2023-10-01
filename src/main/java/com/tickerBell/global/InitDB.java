package com.tickerBell.global;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.entity.Category;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitDB {
    private final InitService initService;

    public static long time;

    @PostConstruct
    public void init() {
        initService.initCategory();
        initService.initMember();
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class InitService {
        private final EntityManager em;
        private final BCryptPasswordEncoder encoder;

        public void initCategory() {
            Category category1 = Category.builder().name(Categories.MUSICAL.getDescription()).build();
            Category category2 = Category.builder().name(Categories.CONCERT.getDescription()).build();
            Category category3 = Category.builder().name(Categories.PLAY.getDescription()).build();
            Category category4 = Category.builder().name(Categories.CLASSIC.getDescription()).build();
            Category category5 = Category.builder().name(Categories.SPORTS.getDescription()).build();

            em.persist(category1);
            em.persist(category2);
            em.persist(category3);
            em.persist(category4);
            em.persist(category5);
        }

        public void initMember() {
            Member member1 = Member.builder().username("abcdefg").password(encoder.encode("abcdefg1")).phone("01012345678").email("email@gmail.com")
                    .role(Role.ROLE_REGISTRANT).authProvider(AuthProvider.KAKAO).build();
            em.persist(member1);
        }
    }
}
