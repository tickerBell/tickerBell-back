package com.tickerBell.domain.emitter.controller;

import com.tickerBell.domain.emitter.service.EmitterService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.global.security.context.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmitterApiController {

    private final EmitterService emitterService;

    @Operation(summary = "SSE 구독 * ")
    @GetMapping(value = "/api/emitter/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe (@RequestHeader(value = "Last-Event-Id", required = false, defaultValue = " ") String lastEventId,
                                 @AuthenticationPrincipal MemberContext memberContext) {

        Member loginMember = memberContext.getMember();
        return emitterService.subscribe(loginMember.getId());
    }
}
