package com.tickerBell.domain.member.service;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.casting.repository.CastingRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.dtos.LoginResponse;
import com.tickerBell.domain.member.dtos.MyPageResponse;
import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.security.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Mock
    private TicketingRepository ticketingRepository;
    @Mock
    private CastingRepository castingRepository;
    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        // given
        String username = "username";
        String password = "password";
        String phone = "phone";
        Role role = Role.ROLE_USER;

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(Member.builder().build());

        // when
        memberService.join(username, password, phone, true, role, null);

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
        Role role = Role.ROLE_USER;

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(Member.builder().build()));

        // when
        assertThatThrownBy(() -> memberService.join(username, password, phone, true, role, AuthProvider.NORMAL))
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

    // todo regenerate Fail Test

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginTest() {
        // given
        String username = "mockUsername";
        String password = "mockPassword";
        String refreshToken = "newRefreshToken";
        String accessToken = "accessToken";
        Member mockMember = Member.builder().username(username).password(password).build();

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(password, mockMember.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(username)).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(username)).thenReturn(refreshToken);
        doNothing().when(jwtTokenProvider).saveRefreshTokenInRedis(username, refreshToken);

        // when
        LoginResponse loginResponse = memberService.login(username, password);

        // then
        verify(memberRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, mockMember.getPassword());
        verify(jwtTokenProvider, times(1)).createAccessToken(username);
        verify(jwtTokenProvider, times(1)).createRefreshToken(username);
        verify(jwtTokenProvider, times(1)).saveRefreshTokenInRedis(username, refreshToken);
        assertThat(loginResponse.getAccessToken()).isEqualTo(accessToken);
        assertThat(loginResponse.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("로그인 회원조회 실패 테스트")
    void loginUsernameFailTest() {
        // given
        String username = "mockUsername";
        String password = "mockPassword";

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.login(username, password))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            assertThat(ex.getStatus()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getStatus().toString());
            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getErrorMessage());
        });
    }

    @Test
    @DisplayName("로그인 비밀번호 검증 실패 테스트")
    void loginPasswordMatchFailTest() {
        // given
        String username = "mockUsername";
        String password = "mockPassword";
        Member mockMember = Member.builder().username(username).password(password).build();

        // stub
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(password, mockMember.getPassword())).thenReturn(false);

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.login(username, password))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
            assertThat(ex.getStatus()).isEqualTo(ErrorCode.INVALID_PASSWORD.getStatus().toString());
            assertThat(ex.getErrorMessage()).isEqualTo(ErrorCode.INVALID_PASSWORD.getErrorMessage());
        });
    }

    @Test
    @DisplayName("일반 회원 마이페이지 조회 테스트")
    void getMyPageTest() {
        // given
        Long memberId = 1L;
        Member generalMember = Member.builder().role(Role.ROLE_USER).build();
        Event event = Event.builder().name("name").startEvent(LocalDateTime.now()).endEvent(LocalDateTime.now()).member(Member.builder().build()).build();
        Ticketing ticketing = Ticketing.builder().member(generalMember).event(event).build();
        List<Ticketing> ticketings = new ArrayList<>();
        ticketings.add(ticketing);
        Casting casting = Casting.builder().event(event).castingName("name").build();
        List<Casting> castings = new ArrayList<>();
        castings.add(casting);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(generalMember));
        when(ticketingRepository.findByMemberId(memberId)).thenReturn(ticketings);
        when(castingRepository.findByEventId(null)).thenReturn(castings);

        // when
        MyPageResponse myPage = memberService.getMyPage(memberId);

        // then
        assertThat(myPage.getStartEvent()).isNotEmpty();
        assertThat(myPage.getEndEvent()).isNotEmpty();
        assertThat(myPage.getIsRegistrant()).isFalse();
        assertThat(myPage.getTicketHolderCounts()).isNull();
        verify(memberRepository, times(1)).findById(memberId);
        verify(ticketingRepository, times(1)).findByMemberId(memberId);
        verify(castingRepository, times(1)).findByEventId(null);
        verifyNoMoreInteractions(ticketingRepository);
    }

    @Test
    @DisplayName("등록자 회원 마이페이지 조회 테스트")
    void getRegistrantMyPageTest() {
        // given
        Long memberId = 1L;
        Member generalMember = Member.builder().role(Role.ROLE_REGISTRANT).build();
        Event event = Event.builder().name("name").startEvent(LocalDateTime.now()).endEvent(LocalDateTime.now()).member(Member.builder().build()).build();
        List<Event> events = new ArrayList<>();
        events.add(event);
        Ticketing ticketing = Ticketing.builder().member(generalMember).event(event).build();
        List<Ticketing> ticketings = new ArrayList<>();
        ticketings.add(ticketing);
        Casting casting = Casting.builder().event(event).castingName("name").build();
        List<Casting> castings = new ArrayList<>();
        castings.add(casting);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(generalMember));
        when(eventRepository.findByMemberIdFetchAll(null)).thenReturn(events);
        when(castingRepository.findByEventId(null)).thenReturn(castings);
        when(ticketingRepository.findByEventId(null)).thenReturn(ticketings);

        // when
        MyPageResponse myPage = memberService.getMyPage(memberId);

        // then
        assertThat(myPage.getStartEvent()).isNotEmpty();
        assertThat(myPage.getEndEvent()).isNotEmpty();
        assertThat(myPage.getIsRegistrant()).isTrue();
        assertThat(myPage.getTicketHolderCounts().size()).isEqualTo(1);
        assertThat(myPage.getTicketHolderCounts().get(0)).isEqualTo(1);
        verify(memberRepository, times(1)).findById(memberId);
    }
}