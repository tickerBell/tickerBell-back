package com.tickerBell.domain.ticketing.controller;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import com.tickerBell.domain.ticketing.service.TicketingService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketingController {

    private final TicketingService ticketingService;

    @PostMapping("/ticketing")
    public ResponseEntity<Response> ticketingEvent(@AuthenticationPrincipal MemberContext memberContext,
                                                   @RequestBody @Valid TicketingRequest request) {
        // 로그인한 회원 객체 조회
        Member loginMember = memberContext.getMember();

        ticketingService.saveTicketing(loginMember.getId(), request);
        return ResponseEntity.ok(new Response("event 예매 완료"));
    }

    @GetMapping("/ticketing")
    public ResponseEntity<Response> ticketingHistory(@AuthenticationPrincipal MemberContext memberContext) {
        List<TicketingResponse> ticketingResponseList = ticketingService.getTicketingHistory(memberContext.getMember().getId());
        // todo: 지난 예매인지 아닌지도 구분 해야 함
        return ResponseEntity.ok(new Response(ticketingResponseList, "예매 정보 반환"));
    }
}
