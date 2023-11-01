package com.tickerBell.domain.event.controller;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.EventResponse;
import com.tickerBell.domain.event.dtos.MainPageDto;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;

    @PostMapping(value = "/api/event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> saveEvent(@RequestPart(name = "request") SaveEventRequest request,
                                              @Parameter(description = "썸네일 이미지") @RequestPart(name = "thumbNailImage") MultipartFile thumbNailImage,
                                              @Parameter(description = "이벤트 이미지") @RequestPart(name = "eventImages") List<MultipartFile> eventImages,
                                              @AuthenticationPrincipal MemberContext memberContext) {
        // 로그인한 회원 객체 조회
        Member loginMember = memberContext.getMember();
        // 이벤트, 특수석, 태그 저장
        eventService.saveEvent(loginMember.getId(), request, thumbNailImage, eventImages);

        return ResponseEntity.ok(new Response("이벤트 등록에 성공하였습니다."));
    }

    @GetMapping("api/events/{category}")
    public ResponseEntity<Response> getEventByCategory(@PathVariable("category") Category category) {
        List<EventListResponse> eventListResponseList = eventService.getEventByCategory(category);
        return ResponseEntity.ok(new Response(eventListResponseList, "카테고리에 해당하는 event 목록 반환 완료"));
    }

    @GetMapping("/api/event/{eventId}")
    public ResponseEntity<Response> getEventById(@PathVariable("eventId") Long eventId) {
        EventResponse eventResponse = eventService.findByIdFetchAll(eventId);
        return ResponseEntity.ok(new Response(eventResponse, "이벤트 상세 데이터 반환 완료"));
    }

    @GetMapping("/api/main")
    public ResponseEntity<Response> getMainPage() {
        MainPageDto mainPage = eventService.getMainPage();

        return ResponseEntity.ok(new Response(mainPage, "메인 페이지 데이터 반환 완료"));
    }
}
