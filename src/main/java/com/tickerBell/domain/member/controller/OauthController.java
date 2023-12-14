package com.tickerBell.domain.member.controller;

import com.tickerBell.domain.member.dtos.login.TokenRequest;
import com.tickerBell.domain.member.service.OauthService;
import com.tickerBell.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    // 플랫폼에서 응답 받음
    @Operation(summary = "auth code 로 토큰 발급", description = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8fd7a1f394d9bbd09fdfdd3827146d73&redirect_uri=" +
            "http://localhost:3000/oauth/kakao")
    @GetMapping("/login/oauth2/code")
    public ResponseEntity<Response> redirect(
            @RequestParam("code") String code
            , @RequestParam(required = false ,value = "state") String state) {

        log.info("auth code 응답 완료: " + code);
        log.info("state 응답 완료: " + state);
        return
                oauthService.redirect(
                        TokenRequest.builder()
                                .registrationId("kakao")
                                .code(code)
                                .state(state)
                                .build());
    }
}

