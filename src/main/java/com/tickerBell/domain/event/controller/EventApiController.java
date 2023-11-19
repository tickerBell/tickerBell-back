package com.tickerBell.domain.event.controller;

import com.tickerBell.domain.event.dtos.*;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;

    @PostMapping(value = "/api/event")
    @Operation(summary = "이벤트 등록 *")
    public ResponseEntity<Response> saveEvent(@RequestBody SaveEventRequest request,
                                              @AuthenticationPrincipal MemberContext memberContext) {
        // 로그인한 회원 객체 조회
        Member loginMember = memberContext.getMember();
        // 이벤트, 특수석, 태그 저장
        eventService.saveEvent(loginMember.getId(), request);

        return ResponseEntity.ok(new Response("이벤트 등록에 성공하였습니다."));
    }

    @GetMapping("api/events/{category}")
    @Operation(summary = "카테고리로 이벤트 조회")
    public ResponseEntity<Response> getEventByCategory(@PathVariable("category") Category category,
                                                       @PageableDefault(size = 10,
                                                               sort = "startEvent",
                                                               direction = Sort.Direction.DESC) Pageable pageable) {
        EventCategoryResponse eventCategoryResponse = eventService.getEventByCategory(category, pageable);
        return ResponseEntity.ok(new Response(eventCategoryResponse, "카테고리에 해당하는 event 목록 반환 완료"));
    }

    @Operation(summary = "이벤트 조회")
    @GetMapping("/api/event/{eventId}")
    public ResponseEntity<Response> getEventById(@PathVariable("eventId") Long eventId) {
        EventResponse eventResponse = eventService.findByIdFetchAll(eventId);
        return ResponseEntity.ok(new Response(eventResponse, "이벤트 상세 데이터 반환 완료"));
    }

    @GetMapping("/api/main")
    @Operation(summary = "메인 페이지", description = "이벤트 등록 *")
    public ResponseEntity<Response> getMainPage() {
        MainPageDto mainPage = eventService.getMainPage();

        return ResponseEntity.ok(new Response(mainPage, "메인 페이지 데이터 반환 완료"));
    }

    @Operation(summary = "전체 event 반환", description = "전체 event 시간 순으로 정렬해서 반환")
    @GetMapping("/api/events")
    public ResponseEntity<Response> getAllEvent(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Page<EventListResponse> eventListResponseList = eventService.findAllEvent(page, size);
        return ResponseEntity.ok(new Response(eventListResponseList, "전체 이벤트 데이터 반환 완료"));
    }
    @Operation(summary = "이벤트 취소 *")
    @PostMapping("/api/event/cancel/{eventId}")
    public ResponseEntity<Response> cancelEventByEventId(@PathVariable("eventId") Long eventId,
                                                         @AuthenticationPrincipal MemberContext memberContext) {
        Member loginMember = memberContext.getMember();
        eventService.cancelEventByEventId(eventId, loginMember.getId());

        return ResponseEntity.ok(new Response("이벤트 취소 성공"));
    }



}
