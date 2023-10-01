package com.tickerBell.domain.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        memberService.join("username", "testPass1!", "010-1234-5679", "email@naver.com", Role.ROLE_REGISTRANT, null);
    }

    @AfterEach
    void afterTest() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("이벤트 저장 api 테스트")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void saveEventTest() throws Exception {
        // given
        SaveEventRequest mockRequest = createMockSaveEventRequest();

        // when
        ResultActions perform = mockMvc.perform(post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("이벤트 등록에 성공하였습니다."));
    }

    private SaveEventRequest createMockSaveEventRequest() {
        SaveEventRequest request = new SaveEventRequest();
//        request.setStartEvent(LocalDateTime.now());
//        request.setEndEvent(LocalDateTime.now());
        request.setName("mockName");
        request.setNormalPrice(10000);
        request.setPremiumPrice(15000);
        request.setSaleDegree(1000F);
        request.setCasting("mockCasting");
        request.setHost("mockHost");
        request.setPlace("mockPlace");
        request.setAge(18);
        request.setIsSpecialA(true);
        request.setIsSpecialB(true);
        request.setIsSpecialC(true);
        request.setCategory(Category.CONCERT);
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        request.setTags(tags);
        return request;
    }
}
