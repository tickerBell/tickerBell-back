package com.tickerBell.domain.ticketing.controller;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.ticketing.dtos.TicketingHistoryNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import com.tickerBell.domain.ticketing.service.TicketingService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "이벤트 회원 예매", description = "회원일 때 이벤트 예매")
    @PostMapping("/ticketing")
    public ResponseEntity<Response> ticketingEvent(@AuthenticationPrincipal MemberContext memberContext,
                                                   @RequestBody @Valid TicketingRequest request) {
        // 로그인한 회원 객체 조회
        Member loginMember = memberContext.getMember();

        ticketingService.saveTicketing(loginMember.getId(), request);
        return ResponseEntity.ok(new Response("회원 event 예매 완료"));
    }

    @Operation(summary = "이벤트 비회원 예매", description = "비회원일 때 이벤트 예매")
    @PostMapping("/ticketing-nonMember")
    public ResponseEntity<Response> ticketingEventNonMember(@RequestBody @Valid TicketingNonMemberRequest request) {
        ticketingService.saveTicketingNonMember(request);
        return ResponseEntity.ok(new Response("비회원 event 예매 완료"));
    }

    @Operation(summary = "이벤트 회원 조회", description = "회원일 때 이벤트 조회")
    @GetMapping("/ticketing")
    public ResponseEntity<Response> ticketingHistory(@AuthenticationPrincipal MemberContext memberContext) {
        List<TicketingResponse> ticketingResponseList = ticketingService.getTicketingHistory(memberContext.getMember().getId());
        return ResponseEntity.ok(new Response(ticketingResponseList, "예매 정보 반환"));
    }
    @Operation(summary = "이벤트 비회원 조회", description = "비회원일 때 이벤트 조회")
    @GetMapping("/ticketing")
    public ResponseEntity<Response> ticketingHistoryNonMember(@RequestBody TicketingHistoryNonMemberRequest request) {
        List<TicketingResponse> ticketingResponseList = ticketingService.getTicketingHistoryNonMember(request);
        return ResponseEntity.ok(new Response(ticketingResponseList, "예매 정보 반환"));
    }
}
