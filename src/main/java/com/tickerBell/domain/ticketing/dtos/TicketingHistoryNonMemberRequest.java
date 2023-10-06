package com.tickerBell.domain.ticketing.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TicketingHistoryNonMemberRequest {
    private String name;
    private String phone;
}
