package com.tickerBell.domain.member.controller;

import com.tickerBell.domain.member.dtos.TokenRequest;
import com.tickerBell.domain.member.service.OauthService;
import com.tickerBell.global.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    // 플랫폼에서 응답 받음
    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<Response> redirect(
            @PathVariable("registrationId") String registrationId
            , @RequestParam("code") String code
            , @RequestParam(required = false ,value = "state") String state) {
        log.info("auth code 응답 완료: " + code);
        log.info("state 응답 완료: " + state);
        return
                oauthService.redirect(
                        TokenRequest.builder()
                                .registrationId(registrationId)
                                .code(code)
                                .state(state)
                                .build());
    }
}

