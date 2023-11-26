package com.tickerBell.domain.map.service;

import com.tickerBell.domain.map.dtos.MapApiRequest;
import com.tickerBell.domain.map.dtos.MapApiResultPath;
import com.tickerBell.domain.map.dtos.MapApiResultXY;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MapApiServiceImpl implements MapApiService {
    private final RestTemplate restTemplate;

    @Value("${naver-cloud-map.api-key-id}")
    private String apiKeyId;

    @Value("${naver-cloud-map.api-key}")
    private String apiKey;

    @Override
    public MapApiResultPath callMapApi(MapApiRequest request) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(request.getQuery());
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();

        URI uri_getXY = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-geocode/v2/geocode")
                .queryParam("query",encode)
                .encode()
                .build()
                .toUri();

        // 아래는 헤더를 넣기 위함
        RequestEntity<Void> header_getXY = RequestEntity
                .get(uri_getXY)
                .header("X-NCP-APIGW-API-KEY-ID", apiKeyId)
                .header("X-NCP-APIGW-API-KEY",apiKey)
                .build();

        ResponseEntity<MapApiResultXY> result_getXY = restTemplate.exchange(header_getXY, MapApiResultXY.class);
        String x = result_getXY.getBody().getAddresses().get(0).getX();
        String y = result_getXY.getBody().getAddresses().get(0).getY();
        String goal = x + "," + y;
        log.info("goal 위도 경도: " + goal);

        URI uri_get_path = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-direction/v1/driving")
                .queryParam("start",request.getStart())
                .queryParam("goal", goal)
                .queryParam("option", "tracomfort")
                .encode()
                .build()
                .toUri();

        // 아래는 헤더를 넣기 위함
        RequestEntity<Void> header_get_path = RequestEntity
                .get(uri_get_path)
                .header("X-NCP-APIGW-API-KEY-ID", apiKeyId)
                .header("X-NCP-APIGW-API-KEY",apiKey)
                .build();

        ResponseEntity<MapApiResultPath> result_get = restTemplate.exchange(header_get_path, MapApiResultPath.class);
        return result_get.getBody();
    }
}
