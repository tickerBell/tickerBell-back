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

    // 이벤트 관련
    private Long ticketingId;
    private Long eventId;
    private String eventName;
    private String place;
    private List<String> casting;
    private LocalDateTime startEvent;
    private LocalDateTime endEvent;

    // 예매 관련
    private Integer ticketHolderCounts; // 예매자 수
    private Boolean isCancelled;
}
