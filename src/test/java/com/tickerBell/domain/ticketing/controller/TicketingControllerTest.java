package com.tickerBell.domain.ticketing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberRequest;
import com.tickerBell.domain.ticketing.dtos.TicketingRequest;
import com.tickerBell.domain.ticketing.service.TicketingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class TicketingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TicketingService ticketingService;
    @Autowired
    private MemberService memberService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        memberService.join("username", "testPass1!", "010-1234-5679", true, Role.ROLE_USER, null);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("회원일 경우 이벤트 예매")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void ticketingEvent() throws Exception {
        // given
        TicketingRequest request = TicketingRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("A-1", "B-2"))
                .build();

        // when
        ResultActions perform = mockMvc.perform(post("/ticketing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 event 예매 완료"));
    }

    @Test
    @DisplayName("비회원일 경우 이벤트 예매")
    public void ticketingEventNonMember() throws Exception {
        // given
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .eventId(1L)
                .selectedSeat(List.of("A-1", "B-2"))
                .name("username")
                .phone("01012345678")
                .build();

        // when
        ResultActions perform = mockMvc.perform(post("/ticketing-nonMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비회원 event 예매 완료"));
    }
}