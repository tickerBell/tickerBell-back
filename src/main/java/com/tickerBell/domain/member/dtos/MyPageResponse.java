package com.tickerBell.domain.member.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {

    // 회원 관련
    private String username;
    private String phone;
    private Boolean isRegistrant; // 등록자 여부

    // 이벤트 관련
    private List<String> eventName;
    private List<List<String>> casting;
    private List<LocalDateTime> startEvent;
    private List<LocalDateTime> endEvent;

    // 예매 관련
    private List<Integer> ticketHolderCounts; // 예매자 수
    private List<Boolean> isCancelled;

    // 페이징 총 개수
    private Long totalCount;
}
