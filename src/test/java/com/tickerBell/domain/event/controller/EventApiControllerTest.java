package com.tickerBell.domain.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.domain.member.dtos.MemberResponse;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ImageRepository imageRepository;
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
    @DisplayName("이벤트 저장 api 테스트")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void saveEventTest() throws Exception {
        // given
        SaveEventRequest mockRequest = createMockSaveEventRequest();
        imageRepository.save(Image.builder().s3Url("url").build());

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
        request.setStartEvent(LocalDateTime.now());
        request.setEndEvent(LocalDateTime.now());
        request.setDailyStartEvent(LocalTime.now());
        request.setName("mockName");
        request.setNormalPrice(10000);
        request.setPremiumPrice(15000);
        request.setSaleDegree(1000F);
        List<String> castings = new ArrayList<>();
        castings.add("casting1");
        request.setCastings(castings);
        List<String> hosts = new ArrayList<>();
        hosts.add("host1");
        request.setHosts(hosts);
        request.setPlace("mockPlace");
        request.setDescription("mockDescription");
        request.setIsAdult(false);
        request.setIsSpecialA(true);
        request.setIsSpecialB(true);
        request.setIsSpecialC(true);
        request.setCategory(Category.CONCERT);
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        request.setTags(tags);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url");
        request.setImageUrls(imageUrls);
        return request;
    }

    @Test
    @DisplayName("카테고리를 통해 이벤트 조회 테스트")
    void getEventByCategoryTest() throws Exception {
        // given
        Category concert = Category.CONCERT;

        // when
        ResultActions perform = mockMvc.perform(get("/api/events/{category}", concert));

        // then
        perform.andExpect(jsonPath("$.message").value("카테고리에 해당하는 event 목록 반환 완료"));
    }

    @Test
    @DisplayName("이벤트 PK를 통한 이벤트 조회 테스트")
    void getEventById() throws Exception {
        // given
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(false).build());
        Long testEventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());

        // when
        ResultActions perform = mockMvc.perform(get("/api/event/{eventId}", testEventId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("mockName"))
                .andExpect(jsonPath("$.data.startEvent").isNotEmpty())
                .andExpect(jsonPath("$.data.endEvent").isNotEmpty())
                .andExpect(jsonPath("$.data.normalPrice").value(10000))
                .andExpect(jsonPath("$.data.premiumPrice").value(15000))
                .andExpect(jsonPath("$.data.discountNormalPrice").value(9000))
                .andExpect(jsonPath("$.data.discountPremiumPrice").value(14000))
                .andExpect(jsonPath("$.data.place").value("mockPlace"))
                .andExpect(jsonPath("$.data.isAdult").value(false))
                .andExpect(jsonPath("$.data.category").value(Category.CONCERT.name()))
                .andExpect(jsonPath("$.data.isSpecialSeatA").value(true))
                .andExpect(jsonPath("$.data.isSpecialSeatB").value(true))
                .andExpect(jsonPath("$.data.isSpecialSeatC").value(true))
                .andExpect(jsonPath("$.message").value("이벤트 상세 데이터 반환 완료"));
        // todo 다대일 관계 andExpect 추가
    }

    @Test
    @DisplayName("이벤트 취소 테스트")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void cancelEventByEventIdFailTest() throws Exception {
        // given
        MemberResponse username = memberService.getMemberByUsername("username");
        Long memberId = username.getMemberId();
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(false).build());
        Long eventId = eventService.saveEvent(memberId, createMockCancelEventRequest());

        // when
        ResultActions perform = mockMvc.perform(post("/api/event/cancel/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이벤트 전체 조회 테스트")
    public void findAllEventTest() throws Exception {
        // given
        int page = 0;
        int size = 10;

        // when
        ResultActions perform = mockMvc.perform(get("/api/events")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("전체 이벤트 데이터 반환 완료"));
    }

    private SaveEventRequest createMockCancelEventRequest() {
        SaveEventRequest request = new SaveEventRequest();
        request.setStartEvent(LocalDateTime.now().plusDays(8));
        request.setEndEvent(LocalDateTime.now());
        request.setDailyStartEvent(LocalTime.now());
        request.setName("mockName");
        request.setNormalPrice(10000);
        request.setPremiumPrice(15000);
        request.setSaleDegree(1000F);
        List<String> castings = new ArrayList<>();
        castings.add("casting1");
        request.setCastings(castings);
        List<String> hosts = new ArrayList<>();
        hosts.add("host1");
        request.setHosts(hosts);
        request.setPlace("mockPlace");
        request.setDescription("mockDescription");
        request.setIsAdult(false);
        request.setIsSpecialA(true);
        request.setIsSpecialB(true);
        request.setIsSpecialC(true);
        request.setCategory(Category.CONCERT);
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        request.setTags(tags);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("url");
        request.setImageUrls(imageUrls);
        return request;
    }
}
