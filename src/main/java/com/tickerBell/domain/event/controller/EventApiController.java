package com.tickerBell.domain.event.controller;

import com.tickerBell.domain.event.dtos.EventListResponse;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        // 이벤트 저장
        Long savedEventId = eventService.saveEvent(
                loginMember.getId(),
                request.getName(),
                request.getStartEvent(),
                request.getEndEvent(),
                request.getNormalPrice(),
                request.getPremiumPrice(),
                request.getSaleDegree(),
                request.getCasting(),
                request.getHost(),
                request.getPlace(),
                request.getAge(),
                request.getCategory());

        // 특수석 저장
        specialSeatService.saveSpecialSeat(savedEventId, request.getIsSpecialA(), request.getIsSpecialB(), request.getIsSpecialC());

        // 태그 저장
        List<String> tags = request.getTags();
        for (String tag : tags) {
            tagService.saveTag(tag, savedEventId, loginMember.getId());
        }

        return ResponseEntity.ok(new Response("이벤트 등록에 성공하였습니다."));
    }


    @PostMapping("api/event/{categoryName}")
    public ResponseEntity<Response> getEventByCategory(@PathVariable("categoryName") String categoryName) {
        List<EventListResponse> eventListResponseList = eventService.getEventByCategory(categoryName);
        return ResponseEntity.ok(new Response(eventListResponseList, "카테고리에 해당하는 event 목록 반환 완료"));
    }
}
