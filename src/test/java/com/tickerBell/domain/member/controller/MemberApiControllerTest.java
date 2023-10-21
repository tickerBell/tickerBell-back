package com.tickerBell.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.member.dtos.JoinMemberRequest;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.domain.member.service.MemberService;
import com.tickerBell.domain.member.dtos.LoginRequest;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

        memberService.join("testUsername", "testPassword1!", "testPhone", true, Role.ROLE_USER, null);
    }

    @AfterEach
    void afterTest() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void joinMemberTest() throws Exception {
        // given
        JoinMemberRequest joinMemberRequest = new JoinMemberRequest();
        joinMemberRequest.setUsername("username");
        joinMemberRequest.setPassword("pass123");
        joinMemberRequest.setPhone("phone");
        joinMemberRequest.setIsRegistration(false);
        joinMemberRequest.setIsKakaoJoin(false);

        // when
        ResultActions perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinMemberRequest)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));
    }

    @Test
    @DisplayName("회원가입 시 작동하는 분기 테스트")
    void checkRoleTest() throws Exception {
        // given
        JoinMemberRequest joinMemberRequest = new JoinMemberRequest();
        joinMemberRequest.setPassword("pass123");
        Boolean isRegistration = true;
        Boolean isKaKaoLogin = true;

        for (int i = 0; i < 2; i++) {
            joinMemberRequest.setUsername("username" + i);
            joinMemberRequest.setIsRegistration(isRegistration);
            joinMemberRequest.setIsKakaoJoin(isKaKaoLogin);

            // when
            ResultActions perform = mockMvc.perform(post("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(joinMemberRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."));

            // then
            Member findMember = memberRepository.findByUsername("username" + i).get();
            if (isRegistration) {
                assertThat(findMember.getRole()).isEqualTo(Role.ROLE_REGISTRANT);
            } else {
                assertThat(findMember.getRole()).isEqualTo(Role.ROLE_USER);
            }
            isRegistration = !isRegistration;
            isKaKaoLogin = !isKaKaoLogin;
        }
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() throws Exception {
        // given
        String username = "testUsername";
        String password = "testPassword1!";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // when
        ResultActions perform = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.message").value("로그인이 완료되었습니다."));
    }

    @Test
    @DisplayName("사용자가 아이디를 잘못 입력했을 시 발생하는 오류 테스트")
    void loginUsernameFailTest() throws Exception {
        // given
        String username = "failUsername";
        String password = "testPassword1!";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // when
        ResultActions perform = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자가 비밀번호를 잘못 입력했을 시 발생하는 오류 테스트")
    void loginPasswordFailTest() throws Exception {
        // given
        String username = "testUsername";
        String password = "failPassword1!";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        // when
        ResultActions perform = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    @WithUserDetails(value = "testUsername", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getMemberTest() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(get("/api/member")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("testUsername"))
                .andExpect(jsonPath("$.data.phone").value("testPhone"))
                .andExpect(jsonPath("$.data.isAdult").value("true"))
                .andExpect(jsonPath("$.data.role").value(Role.ROLE_USER.name()))
                .andExpect(jsonPath("$.message").value("회원 정보 조회 성공"));
    }
}