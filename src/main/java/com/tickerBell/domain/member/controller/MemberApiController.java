package com.tickerBell.domain.member.controller;

import com.tickerBell.domain.member.dtos.JoinMemberRequest;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
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
    public ResponseEntity joinMember(@RequestBody @Valid JoinMemberRequest request) {

        Role role = checkRole(request.getIsRegistration());

        Long joinMemberId = memberService.join(
                request.getUsername(),
                request.getPassword(),
                request.getPhone(),
                request.getEmail(),
                role,
                null
        );

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    private Role checkRole(Boolean isRegistration) {
        if (isRegistration == true) {
            return Role.ROLE_REGISTRANT;
        } else {
            return Role.ROLE_USER;
        }
    }
}
