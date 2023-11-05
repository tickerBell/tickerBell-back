package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberCancelRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;

import java.util.List;

public interface TicketingService {
    Long saveTicketing(Long memberId, TicketingRequest request);
    Long saveTicketingNonMember(TicketingNonMemberRequest request);
    List<TicketingResponse> getTicketingHistory(Long memberId);
    List<TicketingResponse> getTicketingHistoryNonMember(String name, String phone);
    void cancelTicketing(Long memberId, Long ticketingId);
    void cancelTicketingNonMember(TicketingNonMemberCancelRequest request, Long ticketingId);
}
