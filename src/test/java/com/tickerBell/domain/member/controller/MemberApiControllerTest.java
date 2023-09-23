package com.tickerBell.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.member.dtos.JoinMemberRequest;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
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

    @Test
    void checkRoleTest() throws Exception {
        // given
        JoinMemberRequest joinMemberRequest = new JoinMemberRequest();
        joinMemberRequest.setPassword("pass123");
        Boolean isRegistration = true;

        for (int i = 0; i < 2; i++) {
            joinMemberRequest.setUsername("username" + i);
            joinMemberRequest.setIsRegistration(isRegistration);

            // when
            ResultActions perform = mockMvc.perform(post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(joinMemberRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("회원가입이 완료되었습니다."));

            // then
            Member findMember = memberRepository.findByUsername("username" + i);
            if (isRegistration) {
                assertThat(findMember.getRole()).isEqualTo(Role.ROLE_REGISTRANT);
            } else {
                assertThat(findMember.getRole()).isEqualTo(Role.ROLE_USER);
            }
            isRegistration = !isRegistration;
        }
    }
}