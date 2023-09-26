package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.LoginResponse;
import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.security.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ValueOperations valueOperations;

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
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());
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
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(Member.builder().build()));

        // when
        assertThatThrownBy(() -> memberService.join(username, password, phone, email, role, AuthProvider.NORMAL))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 존재하는 아이디입니다.");

        // then
        verify(memberRepository, times(1)).findByUsername(username);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    @DisplayName("토큰 재발급 성공 테스트")
    void regenerateTokenTest() {
        // given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        String mockRefreshToken = "mockRefreshToken";
        String mockUsername = "mockUsername";
        refreshTokenRequest.setRefreshToken(mockRefreshToken);
        Claims mockClaim = Jwts.claims();
        mockClaim.put("username", mockUsername);

        // stub
        when(jwtTokenProvider.isExpiration(refreshTokenRequest.getRefreshToken())).thenReturn(false);
        when(jwtTokenProvider.get(refreshTokenRequest.getRefreshToken())).thenReturn(mockClaim);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(mockUsername)).thenReturn(refreshTokenRequest.getRefreshToken());
        when(jwtTokenProvider.createRefreshToken(mockUsername)).thenReturn("newRefreshToken");
        when(jwtTokenProvider.createAccessToken(mockUsername)).thenReturn("mockAccessToken");

        // when
        LoginResponse result = memberService.regenerateToken(refreshTokenRequest);

        // then
        verify(jwtTokenProvider, times(1)).isExpiration(refreshTokenRequest.getRefreshToken());
        verify(jwtTokenProvider, times(1)).get(refreshTokenRequest.getRefreshToken());
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(mockUsername);
        verify(jwtTokenProvider, times(1)).createRefreshToken(mockUsername);
        verify(jwtTokenProvider, times(1)).createAccessToken(mockUsername);
        verify(jwtTokenProvider, times(1)).saveRefreshTokenInRedis(mockUsername, "newRefreshToken");
        assertThat(result).isNotNull();
        assertThat("newRefreshToken").isEqualTo(result.getRefreshToken());
        assertThat("mockAccessToken").isEqualTo(result.getAccessToken());
    }
}