package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.security.dtos.LoginResponseDto;
import com.tickerBell.global.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public Long join(String username, String password, String phone, String email, Role role, AuthProvider authProvider) {
        // validation 체크
        if(memberRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .email(email)
                .role(role)
                .authProvider(authProvider)
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Override
    public LoginResponseDto regenerateToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        try {
            // Refresh Token 검증
            if (jwtTokenProvider.isExpiration(refreshToken)) {
                throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }

            // Access Token 에서 User email를 가져온다.
            String username = (String) jwtTokenProvider.get(refreshToken).get("username");

            // Redis에서 저장된 Refresh Token 값을 가져온다.
            String findRefreshToken = redisTemplate.opsForValue().get(username);
            if(!refreshToken.equals(findRefreshToken)) {
                // 리프레쉬 토큰 두 개가 안 맞음
                throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
            }

            // 토큰 재발행
            String new_refresh_token = jwtTokenProvider.createRefreshToken(username);
            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .refreshToken(new_refresh_token)
                    .accessToken(jwtTokenProvider.createAccessToken(username))
                    .build();

            // refresh 토큰 업데이트
            jwtTokenProvider.saveRefreshTokenInRedis(username, new_refresh_token);

            return loginResponseDto;
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_UNKNOWN_ERROR);
        }
    }

    @Override
    public LoginResponseDto login(String username, String password) {
        // 사용자가 입력한 Id 검증
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        // 사용자가 입력한 Password 검증
        if(!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // access & refresh Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(findMember.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(findMember.getUsername());

        // refresh Token redis 저장
        jwtTokenProvider.saveRefreshTokenInRedis(findMember.getUsername(), refreshToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
