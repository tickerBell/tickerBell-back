package com.tickerBell.domain.member.service;

import com.tickerBell.domain.casting.repository.CastingRepository;
import com.tickerBell.domain.event.dtos.EventHistoryRegisterResponse;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.event.repository.EventRepository;
import com.tickerBell.domain.member.dtos.*;
import com.tickerBell.domain.member.dtos.join.JoinSmsValidationRequest;
import com.tickerBell.domain.member.dtos.join.JoinSmsValidationResponse;
import com.tickerBell.domain.member.dtos.login.LoginResponse;
import com.tickerBell.domain.member.dtos.login.RefreshTokenRequest;
import com.tickerBell.domain.member.dtos.myPage.MyPageResponse;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import com.tickerBell.domain.ticketing.service.TicketingService;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    private final TicketingService ticketingService;

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

        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

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
                .role(findMember.getRole())
                .build();

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

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(findMember.getRole())
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
    public MyPageResponse getMyPage(Long memberId, PageRequest pageRequest) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 공통 부분 (회원 정보)
        MyPageResponse myPageResponse_v2 = MyPageResponse.builder()
                .username(findMember.getUsername())
                .phone(findMember.getPhone())
                .role(findMember.getRole())
                .build();

        if (findMember.getRole() == Role.ROLE_USER) {
            // 페이징 처리 된 예매 내역
            Page<Ticketing> ticketingListPage = ticketingRepository.findByMemberId(findMember.getId(), pageRequest);

            // 위에서 구한 list 를 dto 로 변환
            // 공연 시간이 현재 시간 보다 지난 공연 인지 여부와
            // selectedSeat 리스트,
            // 각 좌석의 가격 합 추가해서 반환
            List<TicketingResponse> ticketingResponseList = ticketingListPage.stream()
                    .map(ticketing -> TicketingResponse.from(ticketing))
                    .collect(Collectors.toList());

            // PageImpl 을 사용할 경우 page 관련 데이터가 자동으로 추가되서 다시 dto 리스트를 pageImpl 로 감싸줌
            PageImpl<TicketingResponse> ticketingPageResponseList = new PageImpl<>(ticketingResponseList, ticketingListPage.getPageable(), ticketingListPage.getTotalElements());
            myPageResponse_v2.setTicketingResponseList(ticketingPageResponseList);
        }

        if (findMember.getRole() == Role.ROLE_REGISTRANT) {
            // 페이징 처리 된 이벤트 등록 내역
            Page<Event> eventPageList = eventRepository.findByMemberIdPage(findMember.getId(), pageRequest);

            // event -> ticketing -> selectedSeat.size() 를 사용해 전체 예매 좌석 구함
            // 나머진 기존과 거의 비슷
            List<EventHistoryRegisterResponse> eventHistoryRegisterResponseList = eventPageList.stream()
                    .map(event -> EventHistoryRegisterResponse.from(event))
                    .collect(Collectors.toList());

            // PageImpl 을 사용할 경우 page 관련 데이터가 자동으로 추가되서 다시 dto 리스트를 pageImpl 로 감싸줌
            PageImpl<EventHistoryRegisterResponse> eventHistoryResponseList = new PageImpl<>(eventHistoryRegisterResponseList, eventPageList.getPageable(), eventPageList.getTotalElements());
            myPageResponse_v2.setEventHistoryRegisterResponseList(eventHistoryResponseList);
        }
        return myPageResponse_v2;
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

    @Override
    public void updatePassword(Long memberId, String password) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        findMember.updatePassword(passwordEncoder.encode(password));
    }

    @Override
    public Boolean checkCurrentPassword(Long memberId, String password) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return true;
    }
}
