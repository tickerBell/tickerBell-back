package com.tickerBell.domain.member.service;

import com.tickerBell.domain.casting.entity.Casting;
import com.tickerBell.domain.casting.repository.CastingRepository;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.dtos.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
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
        when(memberRepository.findByUsername(mockUsername)).thenReturn(Optional.of(Member.builder().build()));
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

    @Test
    @DisplayName("토큰 재발급 토큰만료 실패 테스트")
    void regenerateExpirationFailTest() {
        // given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        String mockRefreshToken = "mockRefreshToken";
        String mockUsername = "mockUsername";
        refreshTokenRequest.setRefreshToken(mockRefreshToken);
        Claims mockClaim = Jwts.claims();
        mockClaim.put("username", mockUsername);

        // stub
        when(jwtTokenProvider.isExpiration(refreshTokenRequest.getRefreshToken())).thenReturn(true);

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.regenerateToken(refreshTokenRequest))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.REFRESH_TOKEN_EXPIRED);
        });
    }

    @Test
    @DisplayName("토큰 재발급 회원조회 실패 테스트")
    void regenerateTokenMemberFailTest() {
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
        when(memberRepository.findByUsername(mockUsername)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.regenerateToken(refreshTokenRequest))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        });
    }

    @Test
    @DisplayName("토큰 재발급 리프레쉬토큰 비교실패 테스트")
    void regenerateTokenRefreshFailTest() {
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
        when(memberRepository.findByUsername(mockUsername)).thenReturn(Optional.of(Member.builder().build()));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(mockUsername)).thenReturn("fail");

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.regenerateToken(refreshTokenRequest))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        });
    }

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
        PageRequest pageRequest = PageRequest.of(0, 10);
        Long totalCount = 1L;
        PageImpl<Ticketing> ticketingsPage = new PageImpl<>(ticketings, pageRequest, totalCount);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(generalMember));
        when(ticketingRepository.findByMemberIdPage(memberId, pageRequest)).thenReturn(ticketingsPage);
        when(castingRepository.findByEventId(null)).thenReturn(castings);

        // when
        MyPageListResponse myPageListResponse = memberService.getMyPage(memberId, pageRequest);

        // then
        assertThat(myPageListResponse.getRole()).isEqualTo(generalMember.getRole());
        assertThat(myPageListResponse.getMyPageResponse().get(0).getTicketHolderCounts()).isNull();
        verify(memberRepository, times(1)).findById(memberId);
        verify(ticketingRepository, times(1)).findByMemberIdPage(memberId, pageRequest);
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
        PageRequest pageRequest = PageRequest.of(0, 10);
        Long totalCount = 1L;
        PageImpl<Event> eventsPage = new PageImpl<>(events, pageRequest, totalCount);

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(generalMember));
        when(eventRepository.findByMemberIdFetchAllPage(null, pageRequest)).thenReturn(eventsPage);
        when(castingRepository.findByEventId(null)).thenReturn(castings);
        when(ticketingRepository.findByEventId(null)).thenReturn(ticketings);

        // when
        MyPageListResponse myPageListResponse = memberService.getMyPage(memberId, pageRequest);

        // then
        assertThat(myPageListResponse.getMyPageResponse()).isNotEmpty();
        assertThat(myPageListResponse.getRole()).isEqualTo(generalMember.getRole());
        assertThat(myPageListResponse.getMyPageResponse().get(0).getTicketHolderCounts()).isNotNull();
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    @DisplayName("회원 조회 로직 테스트")
    void getMemberTest() {
        // given
        Long memberId = 1L;
        Member mockMember = Member.builder()
                .username("username")
                .phone("1234")
                .role(Role.ROLE_REGISTRANT)
                .isAdult(true)
                .build();

        // stub
        when(memberRepository.findById(memberId)).thenAnswer(invocation -> {
            setPrivateField(mockMember, "id", 1L);
            return Optional.of(mockMember);
        });

        // when
        MemberResponse memberResponse = memberService.getMember(memberId);

        // then
        assertThat(memberResponse.getMemberId()).isEqualTo(memberId);
        assertThat(memberResponse.getUsername()).isEqualTo("username");
        assertThat(memberResponse.getRole()).isEqualTo(Role.ROLE_REGISTRANT);
        assertThat(memberResponse.getIsAdult()).isTrue();
    }

    private void setPrivateField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    @Test
    @DisplayName("회원 조회 실패 테스트")
    void getMemberFailTest() {
        // given
        Long memberId = 1L;
        Member mockMember = Member.builder()
                .username("username")
                .phone("1234")
                .role(Role.ROLE_REGISTRANT)
                .isAdult(true)
                .build();

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.getMember(memberId))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        });
    }

    @Test
    @DisplayName("회원 username 으로 조회 로직 테스트")
    void getMemberByUsernameTest() {
        // given
        Long memberId = 1L;
        Member mockMember = Member.builder()
                .username("username")
                .phone("1234")
                .role(Role.ROLE_REGISTRANT)
                .isAdult(true)
                .build();

        // stub
        Member spyMember = spy(mockMember);
        when(spyMember.getId()).thenReturn(memberId);
        when(memberRepository.findByUsername(any(String.class))).thenReturn(Optional.of(spyMember));

        // when
        MemberResponse memberResponse = memberService.getMemberByUsername(mockMember.getUsername());

        // then
        assertThat(memberResponse.getMemberId()).isEqualTo(memberId);
        assertThat(memberResponse.getUsername()).isEqualTo("username");
        assertThat(memberResponse.getRole()).isEqualTo(Role.ROLE_REGISTRANT);
        assertThat(memberResponse.getIsAdult()).isTrue();
        verify(memberRepository, times(1)).findByUsername(any(String.class));
    }

    @Test
    @DisplayName("회원 username 으로 조회 실패 테스트")
    void getMemberByUsernameFailTest() {
        // given
        String username = "username";

        // stub
        when(memberRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> memberService.getMemberByUsername(username))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("사용자 비밀번호 변경 테스트")
    void updatePasswordTest() {
        // given
        Long memberId = 1L;
        String password = "password";
        Member currentMember = Member.builder().password("currentPassword").build();

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(currentMember));
        when(passwordEncoder.encode(password)).thenReturn("encodePassword");

        // when
        memberService.updatePassword(memberId, password);

        // then
        assertThat(currentMember.getPassword()).isEqualTo("encodePassword");
        verify(memberRepository, times(1)).findById(memberId);
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    @DisplayName("사용자 비밀번호 변경 실패 테스트")
    void updatePasswordFailTest() {
        // given
        Long memberId = 1L;
        String password = "password";

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.updatePassword(memberId, password))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        });
        verify(memberRepository, times(1)).findById(memberId);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("사용자 현재 비밀번호 확인 테스트")
    void checkCurrentPasswordTest() {
        Long memberId = 1L;
        String password = "password";
        Member currentMember = Member.builder().password(password).build();

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(currentMember));
        when(passwordEncoder.matches(password, currentMember.getPassword())).thenReturn(true);

        // when
        Boolean currentPassword = memberService.checkCurrentPassword(memberId, password);

        // then
        assertThat(currentPassword).isEqualTo(true);
        verify(memberRepository, times(1)).findById(memberId);
        verify(passwordEncoder, times(1)).matches(password, currentMember.getPassword());
    }

    @Test
    @DisplayName("사용자 현재 비밀번호 확인 회원조회 실패 테스트")
    void checkCurrentPasswordMemberFailTest() {
        Long memberId = 1L;
        String password = "password";

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.checkCurrentPassword(memberId, password))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        });
        verify(memberRepository, times(1)).findById(memberId);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("사용자 현재 비밀번호 확인 비밀번호 매치 실패 테스트")
    void checkCurrentPasswordMatchFailTest() {
        Long memberId = 1L;
        String password = "password";
        Member currentMember = Member.builder().password(password).build();

        // stub
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(currentMember));
        when(passwordEncoder.matches(password, currentMember.getPassword())).thenReturn(false);

        // when
        AbstractObjectAssert<?, CustomException> extracting = assertThatThrownBy(() -> memberService.checkCurrentPassword(memberId, password))
                .isInstanceOf(CustomException.class)
                .extracting(ex -> (CustomException) ex);

        // then
        extracting.satisfies(ex -> {
            assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_NOT_MATCH);
        });
        verify(memberRepository, times(1)).findById(memberId);
        verify(passwordEncoder, times(1)).matches(password, currentMember.getPassword());
    }
}