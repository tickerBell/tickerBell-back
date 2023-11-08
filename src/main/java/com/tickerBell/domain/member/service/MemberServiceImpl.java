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
import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final SmsService smsService;
    private final TicketingRepository ticketingRepository;
    private final CastingRepository castingRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Long join(String username, String password, String phone, Boolean isAdult, Role role, AuthProvider authProvider) {
        // validation 체크
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .isAdult(isAdult)
                .role(role)
                .authProvider(authProvider)
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Override
    public LoginResponse regenerateToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        // Refresh Token 검증
        if (jwtTokenProvider.isExpiration(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // Access Token 에서 User email를 가져온다.
        String username = (String) jwtTokenProvider.get(refreshToken).get("username");

        // Redis에서 저장된 Refresh Token 값을 가져온다.
        String findRefreshToken = redisTemplate.opsForValue().get(username);
        if (!refreshToken.equals(findRefreshToken)) {
            // 리프레쉬 토큰 두 개가 안 맞음
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        // 토큰 재발행
        String new_refresh_token = jwtTokenProvider.createRefreshToken(username);
        LoginResponse loginResponse = LoginResponse.builder()
                .refreshToken(new_refresh_token)
                .accessToken(jwtTokenProvider.createAccessToken(username))
                .build();

        // refresh 토큰 업데이트
        jwtTokenProvider.saveRefreshTokenInRedis(username, new_refresh_token);

        return loginResponse;
    }


    @Override
    public LoginResponse login(String username, String password) {
        // 사용자가 입력한 Id 검증
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        // 사용자가 입력한 Password 검증
        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // access & refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(findMember.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(findMember.getUsername());

        // refresh Token redis 저장
        jwtTokenProvider.saveRefreshTokenInRedis(findMember.getUsername(), refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public JoinSmsValidationResponse joinSmsValidation(JoinSmsValidationRequest request) {
        Random random = new Random();
        // 1000부터 9999 랜덤 생성
        int randomNumber = random.nextInt(9000) + 1000;
        String content = "[tickerBell] 본인확인 인증번호 [" + randomNumber + "] 입니다.";
        try {
            smsService.sendSms(request.getPhone(), content);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SMS_SEND_FAIL);
        }
        return new JoinSmsValidationResponse(randomNumber);
    }

    @Override
    public MyPageListResponse getMyPage(Long memberId, Pageable pageable) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        MyPageListResponse myPageListResponse = new MyPageListResponse();
        myPageListResponse.setUsername(findMember.getUsername());
        myPageListResponse.setPhone(findMember.getPhone());

        List<MyPageResponse> myPageResponses = new ArrayList<>();

        if (findMember.getRole().equals(Role.ROLE_USER)) {

            // 일반 사용자 myPage
            myPageListResponse.setIsRegistrant(false);

            Page<Ticketing> findTicketingsPage = ticketingRepository.findByMemberIdPage(memberId, pageable);
            long totalCount = findTicketingsPage.getTotalElements();
            List<Ticketing> findTicketings = findTicketingsPage.getContent();
            myPageListResponse.setTotalCount(totalCount);

            for (Ticketing findTicketing : findTicketings) {
                Event findEvent = findTicketing.getEvent();
                List<Casting> findCastings = castingRepository.findByEventId(findEvent.getId());
                List<String> castings = new ArrayList<>();
                for (Casting findCasting : findCastings) {
                    castings.add(findCasting.getCastingName());
                }

                MyPageResponse myPageResponse = new MyPageResponse();
                myPageResponse.setEventName(findEvent.getName());
                myPageResponse.setCasting(castings);
                myPageResponse.setStartEvent(findEvent.getStartEvent());
                myPageResponse.setEndEvent(findEvent.getEndEvent());

                myPageResponses.add(myPageResponse);
            }

        } else {

            // 등록자 myPage
            myPageListResponse.setIsRegistrant(true);

            Page<Event> findEventsPage = eventRepository.findByMemberIdFetchAllPage(findMember.getId(), pageable);
            long totalCount = findEventsPage.getTotalElements();
            List<Event> findEvents = findEventsPage.getContent();
            myPageListResponse.setTotalCount(totalCount);


            for (Event findEvent : findEvents) {

                List<Casting> findCastings = castingRepository.findByEventId(findEvent.getId());

                List<String> castings = new ArrayList<>();
                for (Casting findCasting : findCastings) {
                    castings.add(findCasting.getCastingName());
                }

                List<Ticketing> findTicketings = ticketingRepository.findByEventId(findEvent.getId());

                MyPageResponse myPageResponse = new MyPageResponse();
                myPageResponse.setEventName(findEvent.getName());
                myPageResponse.setCasting(castings);
                myPageResponse.setStartEvent(findEvent.getStartEvent());
                myPageResponse.setEndEvent(findEvent.getEndEvent());
                myPageResponse.setIsCancelled(findEvent.getIsCancelled());
                myPageResponse.setTicketHolderCounts(findTicketings.size());

                myPageResponses.add(myPageResponse);
            }

        }

        myPageListResponse.setMyPageResponse(myPageResponses);

        return myPageListResponse;
    }

    @Override
    public MemberResponse getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return MemberResponse.from(findMember);
    }

    @Override
    public MemberResponse getMemberByUsername(String username) {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        return MemberResponse.from(findMember);
    }
}
