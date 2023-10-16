package com.tickerBell.domain.map.controller;

import com.tickerBell.domain.map.dtos.MapApiRequest;
import com.tickerBell.domain.map.dtos.MapApiResultPath;
import com.tickerBell.domain.map.dtos.MapApiResultXY;
import com.tickerBell.domain.map.service.MapApiService;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MapApiController {

    private final MapApiService mapApiService;

    @Operation(summary = "naver api 호출", description = "네이버 api 호출. start 주소의 위도 경도와 목적지의 위도 경도의 네비게이션 정보 반환")
    @PostMapping("/naver-api/path")
    public ResponseEntity<Response> callNaverApi(@RequestBody MapApiRequest request) {
        MapApiResultPath mapApiResultPath;
        try {
            mapApiResultPath = mapApiService.callMapApi(request);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.MAP_API_CALL_FAIL);
        }
        return ResponseEntity.ok(new Response(mapApiResultPath, "네이버 api 호출 완료. 최단 경로 반환"));
    }
}
