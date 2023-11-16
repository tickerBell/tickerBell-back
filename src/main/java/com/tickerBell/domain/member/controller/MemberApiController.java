package com.tickerBell.domain.member.controller;

import com.tickerBell.domain.member.dtos.*;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
                request.getIsAdult(),
                role,
                request.getIsKakaoJoin() ? AuthProvider.KAKAO : AuthProvider.NORMAL
        );

        return ResponseEntity.ok(new Response("회원가입이 완료되었습니다."));
    }

    @Operation(summary = "refresh 토큰 요청")
    @PostMapping("/reissue")
    public ResponseEntity<Response> regenerateToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        LoginResponse loginResponse = memberService.regenerateToken(refreshTokenRequest);

        return ResponseEntity.ok(new Response(loginResponse, "refresh 토큰으로 access 토큰 재발행"));
    }

    @Operation(summary = "로그인 요청")
    @PostMapping("/api/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(new Response(loginResponse, "로그인이 완료되었습니다."));
    }

    @Operation(summary = "문자인증 회원가입")
    @PostMapping("/api/join/sms-validation")
    public ResponseEntity<Response> joinSmsValidation(@RequestBody JoinSmsValidationRequest request) {
        JoinSmsValidationResponse joinSmsValidationResponse = memberService.joinSmsValidation(request);
        return ResponseEntity.ok(new Response(joinSmsValidationResponse, "발송한 랜덤 코드 4자리 반환"));
    }

    private Role checkRole(Boolean isRegistration) {
        if (isRegistration == true) {
            return Role.ROLE_REGISTRANT;
        } else {
            return Role.ROLE_USER;
        }
    }

    @Operation(summary = "회원 정보 조회 *")
    @GetMapping("/api/member")
    public ResponseEntity<Response> getMember(@AuthenticationPrincipal MemberContext memberContext) {
        Member loginMember = memberContext.getMember();

        MemberResponse memberResponse = memberService.getMember(loginMember.getId());

        return ResponseEntity.ok(new Response(memberResponse, "회원 정보 조회 성공"));
    }

    @Operation(summary = "마이 페이지 조회 *")
    @GetMapping("/api/member/my")
    public ResponseEntity<Response> myPage(@AuthenticationPrincipal MemberContext memberContext,
                                           @PageableDefault(size = 10,
                                           sort = "createdDate",
                                           direction = Sort.Direction.DESC)Pageable pageable) {

        Member loginMember = memberContext.getMember();
        MyPageListResponse myPageListResponse = memberService.getMyPage(loginMember.getId(), pageable);

        return ResponseEntity.ok(new Response(myPageListResponse, "마이페이지 조회 성공"));
    }

    @PutMapping("/api/member/password")
    @Operation(summary = "사용자 비밀번호 변경 *")
    public ResponseEntity<Response> updateMemberPassword(@RequestBody MemberPasswordRequest request,
                                                         @AuthenticationPrincipal MemberContext memberContext) {
        Member loginMember = memberContext.getMember();

        memberService.updatePassword(loginMember.getId(), request.getPassword());

        return ResponseEntity.ok(new Response("비밀번호 변경 성공"));
    }
}
