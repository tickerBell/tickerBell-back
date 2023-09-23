package com.tickerBell.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.member.dtos.JoinMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void joinMemberTest() throws Exception {
        // given
        JoinMemberRequest joinMemberRequest = new JoinMemberRequest();
        joinMemberRequest.setUsername("username");
        joinMemberRequest.setPassword("pass123");
        joinMemberRequest.setPhone("phone");
        joinMemberRequest.setEmail("email");
        joinMemberRequest.setIsRegistration(false);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinMemberRequest)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }
}