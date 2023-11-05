package com.tickerBell.domain.ticketing.dtos;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingNonMemberCancelRequest {
    private String name;
    private String phone;
}
