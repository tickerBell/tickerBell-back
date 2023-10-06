package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.ticketing.dtos.TicketingHistoryNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;

import java.util.List;

public interface TicketingService {
    void saveTicketing(Long memberId, TicketingRequest request);
    void saveTicketingNonMember(TicketingNonMemberRequest request);
    List<TicketingResponse> getTicketingHistory(Long memberId);
    List<TicketingResponse> getTicketingHistoryNonMember(TicketingHistoryNonMemberRequest request);
}
