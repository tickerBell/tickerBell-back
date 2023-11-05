package com.tickerBell.domain.image.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {

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
        memberService.join("username", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void afterTest() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("이미지 등록 api 테스트")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void uploadEventImageTest() throws Exception {
        // given
        MockMultipartFile thumbNailImage = new MockMultipartFile("thumbNailImage", "image1.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile eventImage1 = new MockMultipartFile("eventImages", "image1.jpg", "image/jpeg", new byte[0]);
        MockMultipartFile eventImage2 = new MockMultipartFile("eventImages", "image2.png", "image/png", new byte[0]);
        images.add(eventImage1);
        images.add(eventImage2);

        // when
        ResultActions perform = mockMvc.perform(multipart("/api/image")
                .file(thumbNailImage)
                .file(eventImage1)
                .file(eventImage2)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        perform.andExpect(status().isOk());
    }
}