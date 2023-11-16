package com.tickerBell.domain.map.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tickerBell.domain.map.dtos.MapApiRequest;
import com.tickerBell.domain.map.dtos.MapApiResultPath;
import com.tickerBell.domain.map.service.MapApiService;
import com.tickerBell.domain.map.service.MapApiServiceImpl;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class MapApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MapApiServiceImpl mapApiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("naver api 호출")
    public void testCallNaverApi() throws Exception {
        // given
        MapApiResultPath mockMapApiResultPath = new MapApiResultPath();
        MapApiRequest request = MapApiRequest.builder()
                .query("성수동 11-1")
                .start("126.9652628,37.4750974")
                .build();
        // stub
        when(mapApiService.callMapApi(any(MapApiRequest.class))).thenReturn(mockMapApiResultPath);

        // when
        ResultActions perform = mockMvc.perform(post("/naver-api/path")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("네이버 api 호출 완료. 최단 경로 반환"));
    }

    @Test
    @DisplayName("naver api 호출 중 에러")
    public void testCallNaverApiWithException() throws Exception {
        // given
        MapApiRequest request = MapApiRequest.builder()
                .query("성수동 11-1")
                .start("126.9652628,37.4750974")
                .build();

        // 예외 상황에 대한 stubbing
        when(mapApiService.callMapApi(any(MapApiRequest.class)))
                .thenThrow(new CustomException(ErrorCode.MAP_API_CALL_FAIL));

        // when
        ResultActions perform = mockMvc.perform(post("/naver-api/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data").value("naver map api 호출 중 에러가 발생했습니다.")) // JSON 응답 내용을 확인
                .andExpect(jsonPath("$.message").value("커스텀 예외 반환"));
    }

}