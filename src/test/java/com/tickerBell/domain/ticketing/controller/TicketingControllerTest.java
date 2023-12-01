package com.tickerBell.domain.ticketing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tickerBell.domain.event.dtos.SaveEventRequest;
import com.tickerBell.domain.event.entity.Category;
import com.tickerBell.domain.event.service.EventService;
import com.tickerBell.domain.image.entity.Image;
import com.tickerBell.domain.image.repository.ImageRepository;
import com.tickerBell.domain.image.service.ImageS3Handler;
import com.tickerBell.domain.image.service.ImageService;

import com.tickerBell.domain.member.dtos.MemberResponse;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.service.MemberService;
import com.tickerBell.domain.ticketing.dtos.TicketingNonMemberCancelRequest;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    private EventService eventService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageS3Handler imageS3Handler;
    @Autowired

    private ImageRepository imageRepository;

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
        // 등록자 저장
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // image 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        // event 저장
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());
        TicketingRequest request = TicketingRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
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
        // 등록자 저장
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // image 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        // event 저장
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .name("nonMember")
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

    @Test
    @DisplayName("회원일 때 예매내역 조회")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void ticketingHistory() throws Exception {
        // given
        // event 저장
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // image 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());

        // ticketing 저장
        TicketingRequest request = TicketingRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .build();
        MemberResponse memberResponse = memberService.getMemberByUsername("username");
        Long ticketingId = ticketingService.saveTicketing(memberResponse.getMemberId(), request);


        // when
        ResultActions perform = mockMvc.perform(get("/ticketing")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 예매 정보 반환"))
                .andExpect(jsonPath("$.data.content[0].ticketingId").value(ticketingId))
                .andExpect(jsonPath("$.data.content[0].payment").value(2 * (15000-1000))) // 회원일 땐 세일 o
                .andExpect(jsonPath("$.data.content[0].selectedSeatResponseList", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].eventHistoryResponse").exists())
                .andExpect(jsonPath("$.data.content[0].isPast").value(false));
    }

    @Test
    @DisplayName("비회원일 때 예매내역 조회")
    public void ticketingHistoryNonMember() throws Exception {
        // given
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // image 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        // event 저장
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());

        // ticketing 저장
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .name("nonMember")
                .phone("01012345678")
                .build();
        Long ticketingId = ticketingService.saveTicketingNonMember(request);

        // when
        ResultActions perform = mockMvc.perform(get("/ticketing-nonMember")
                        .param("name", "nonMember")
                        .param("phone", "01012345678"));


        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비회원 예매 정보 반환"))
                .andExpect(jsonPath("$.data.content[0].ticketingId").value(ticketingId))
                .andExpect(jsonPath("$.data.content[0].payment").value(2 * 15000)) // 비회원일 땐 세일 x
                .andExpect(jsonPath("$.data.content[0].selectedSeatResponseList", hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].eventHistoryResponse").exists())
                .andExpect(jsonPath("$.data.content[0].isPast").value(false));
    }

    @Test
    @DisplayName("회원일 때 예매 취소")
    @WithUserDetails(value = "username", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void ticketingCancel() throws Exception {
        // given
        // event 저장
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // image 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());

        // ticketing 저장
        TicketingRequest request = TicketingRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .build();
        MemberResponse memberResponse = memberService.getMemberByUsername("username");
        Long ticketingId = ticketingService.saveTicketing(memberResponse.getMemberId(), request);

        // when
        ResultActions perform = mockMvc.perform(delete("/ticketing/{ticketingId}", ticketingId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원 예매 내역 취소 완료"));
    }

    @Test
    @DisplayName("비회원일 때 예매 취소")
    public void ticketingCancelNonMember() throws Exception {
        // given
        // event 저장
        Long testUserId = memberService.join("testUsername", "testPass1!", "010-1234-5679", true, Role.ROLE_REGISTRANT, null);
        // 이미지 저장
        imageRepository.save(Image.builder().s3Url("url").isThumbnail(true).build());
        Long eventId = eventService.saveEvent(testUserId, createMockSaveEventRequest());

        // ticketing 저장
        TicketingNonMemberRequest request = TicketingNonMemberRequest.builder()
                .eventId(eventId)
                .selectedSeat(List.of("A-1", "B-2"))
                .paymentId("paymentId")
                .selectedDate(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .name("nonMember")
                .phone("01012345678")
                .build();
        Long ticketingId = ticketingService.saveTicketingNonMember(request);

        TicketingNonMemberCancelRequest requestCancel = TicketingNonMemberCancelRequest.builder()
                .name("nonMember")
                .phone("01012345678")
                .build();

        // when
        ResultActions perform = mockMvc.perform(delete("/ticketing-nonMember/{ticketingId}", ticketingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestCancel)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비회원 예매 내역 취소 완료"));
    }





    private SaveEventRequest createMockSaveEventRequest() {
        SaveEventRequest request = new SaveEventRequest();
        request.setStartEvent(LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        request.setEndEvent(LocalDateTime.now().plusDays(1).plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0).plusHours(2));
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
        request.setIsAdult(false);
        request.setIsSpecialA(true);
        request.setIsSpecialB(true);
        request.setIsSpecialC(true);
        request.setCategory(Category.CONCERT);
        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        request.setTags(tags);
        request.setThumbNailUrl("url");
//        request.setImageUrls(List.of("url"));
        return request;
    }
}