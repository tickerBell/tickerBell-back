package com.tickerBell.domain.event.controller;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.specialseat.service.SpecialSeatService;
import com.tickerBell.domain.tag.service.TagService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;
    private final SpecialSeatService specialSeatService;
    private final TagService tagService;

    @PostMapping("/api/event")
    public ResponseEntity<Response> saveEvent(@RequestBody SaveEventRequest request,
                                              @AuthenticationPrincipal MemberContext memberContext) {
        // 로그인한 회원 객체 조회
        Member loginMember = memberContext.getMember();
        // 이벤트, 특수석, 태그 저장
        eventService.saveEvent(loginMember.getId(), request);
        return ResponseEntity.ok(new Response("이벤트 등록에 성공하였습니다."));
    }

    @GetMapping("api/events/{category}")
    public ResponseEntity<Response> getEventByCategory(@PathVariable("category") Category category) {
        List<EventListResponse> eventListResponseList = eventService.getEventByCategory(category);
        return ResponseEntity.ok(new Response(eventListResponseList, "카테고리에 해당하는 event 목록 반환 완료"));
    }
}
