package com.tickerBell.domain.member.dtos.myPage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tickerBell.domain.event.dtos.EventHistoryRegisterResponse;
import com.tickerBell.domain.event.dtos.EventHistoryUserResponse;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
public class MyPageResponse_V2 {
    // 회원 관련
    private String username;
    private String phone;
    private Role role;

    // 예매자
    private Page<TicketingResponse> ticketingResponseList; // 예매 관련 정보 조회 (예매자)

    // 등록자
    private Page<EventHistoryRegisterResponse> eventHistoryRegisterResponseList; // 등록한 eventList
}
