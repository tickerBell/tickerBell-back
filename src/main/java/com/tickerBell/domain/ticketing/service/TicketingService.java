package com.tickerBell.domain.ticketing.service;

import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberCancelRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketingService {
    Long saveTicketing(Long memberId, TicketingRequest request);
    Long saveTicketingNonMember(TicketingNonMemberRequest request);
    Page<TicketingResponse> getTicketingHistory(Long memberId, int page, int size);
    Page<TicketingResponse> getTicketingHistoryNonMember(String name, String phone, int page, int size);
    void cancelTicketing(Long memberId, Long ticketingId);
    void cancelTicketingNonMember(TicketingNonMemberCancelRequest request, Long ticketingId);
}
