package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        // given
        String username = "username";
        String password = "password";
        String phone = "phone";
        String email = "email";
        Role role = Role.ROLE_USER;

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(null);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(Member.builder().build());

        // when
        memberService.join(username, password, phone, email, role, null);

        // then
        verify(memberRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(password);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void joinFailTest() {
        // given
        String username = "username";
        String password = "password";
        String phone = "phone";
        String email = "email";
        Role role = Role.ROLE_USER;

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Member.builder().build());

        // when
        assertThatThrownBy(() -> memberService.join(username, password, phone, email, role, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicated Username");

        // then
        verify(memberRepository, times(1)).findByUsername(username);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(memberRepository);
    }
}