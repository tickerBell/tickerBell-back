package com.tickerBell.domain.member.controller;

import com.tickerBell.domain.member.dtos.JoinMemberRequest;
import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.dtos.LoginDto;
import com.tickerBell.global.security.dtos.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    @Operation(summary = "회원 가입 요청")
    public ResponseEntity<Response> joinMember(@RequestBody @Valid JoinMemberRequest request) {

        Role role = checkRole(request.getIsRegistration());

        Long joinMemberId = memberService.join(
                request.getUsername(),
                request.getPassword(),
                request.getPhone(),
                request.getEmail(),
                role,
                request.getIsKakaoJoin() ? AuthProvider.KAKAO : AuthProvider.NORMAL
        );

        return ResponseEntity.ok(new Response("회원가입이 완료되었습니다."));
    }

    @Operation(summary = "refresh 토큰 요청")
    @PostMapping("/reissue")
    public ResponseEntity<Response> regenerateToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        LoginResponseDto loginResponseDto = memberService.regenerateToken(refreshTokenRequest);

        return ResponseEntity.ok(new Response(loginResponseDto, "refresh 토큰으로 access 토큰 재발행"));
    }

    @PostMapping("/api/login")
    public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponse = memberService.login(loginDto.getUsername(), loginDto.getPassword());

        return ResponseEntity.ok(new Response(loginResponse, "로그인이 완료되었습니다."));
    }

    private Role checkRole(Boolean isRegistration) {
        if (isRegistration == true) {
            return Role.ROLE_REGISTRANT;
        } else {
            return Role.ROLE_USER;
        }
    }
}
