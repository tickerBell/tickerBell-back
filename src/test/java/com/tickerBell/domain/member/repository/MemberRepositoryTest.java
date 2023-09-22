package com.tickerBell.domain.member.repository;

import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void initData() {
        Member initMember = Member.builder()
                .username("initUsername")
                .password("initPassword")
                .phone("initPhone")
                .role(Role.ROLE_REGISTRANT)
                .authProvider(AuthProvider.KAKAO)
                .build();
        memberRepository.save(initMember);
    }

    @Test
    @DisplayName("회원 저장 테스트")
    void saveTest() { //
        // given
        Member member = Member.builder()
                .username("username")
                .password("password")
                .phone("phone")
                .role(Role.ROLE_USER)
                .authProvider(AuthProvider.KAKAO)
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isEqualTo(member.getId());
        assertThat(savedMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getPhone()).isEqualTo(member.getPhone());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
        assertThat(savedMember.getRole().getDeclaringClass()).isEqualTo(member.getRole().getDeclaringClass());
        assertThat(savedMember.getAuthProvider()).isEqualTo(member.getAuthProvider());
        assertThat(savedMember.getAuthProvider().getDeclaringClass()).isEqualTo(member.getAuthProvider().getDeclaringClass());
    }

    @Test
    void findByUsernameTest() {
        // given
        String username = "initUsername";

        // when
        Member findMember = memberRepository.findByUsername(username);

        // then
        assertThat(findMember.getUsername()).isEqualTo(username);
    }
}