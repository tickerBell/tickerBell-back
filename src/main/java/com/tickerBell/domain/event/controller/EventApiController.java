package com.tickerBell.domain.event.controller;

import com.tickerBell.domain.category.entity.Categories;
import com.tickerBell.domain.category.service.CategoryService;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.specialseat.service.SpecialSeatService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.context.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventApiController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final SpecialSeatService specialSeatService;

    @PostMapping("/api/event")
    public ResponseEntity<Response> saveEvent(@RequestBody SaveEventRequest request,
                                              @AuthenticationPrincipal MemberContext memberContext) {

        Member loginMember = memberContext.getMember();
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
                request.getAge());

        List<Categories> categories = request.getCategories();
        for (Categories category : categories) {
            categoryService.saveCategory(savedEventId, category);
        }

        specialSeatService.saveSpecialSeat(savedEventId, request.getIsSpecialA(), request.getIsSpecialB(), request.getIsSpecialC());

        return ResponseEntity.ok(new Response("이벤트 등록에 성공하였습니다."));
    }
}
