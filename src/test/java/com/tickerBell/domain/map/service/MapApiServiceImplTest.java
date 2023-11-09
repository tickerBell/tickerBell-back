package com.tickerBell.domain.map.service;


import com.tickerBell.domain.map.dtos.MapApiRequest;
import com.tickerBell.domain.map.dtos.MapApiResultPath;
import com.tickerBell.domain.map.dtos.MapApiResultXY;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapApiServiceImplTest {
    @InjectMocks
    private MapApiServiceImpl mapApiService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(mapApiService, "apiKeyId", "mock_apiKey_id");
        ReflectionTestUtils.setField(mapApiService, "apiKey", "mock_apiKey");
    }
    @Test
    public void testCallMapApi() {
        // given
        MapApiRequest request = MapApiRequest.builder()
                .query("성수동 11-1")
                .start("126.9652628,37.4750974")
                .build();
        MapApiResultXY resultXY = new MapApiResultXY();
        MapApiResultXY.Address address = new MapApiResultXY.Address();
        address.setX("1");
        address.setY("1");
        resultXY.setAddresses(List.of(address));
        ResponseEntity<MapApiResultXY> xyResponseEntity = new ResponseEntity<>(resultXY, HttpStatus.OK);


        // stub
        when(restTemplate.exchange(any(), eq(MapApiResultXY.class)))
                .thenReturn(xyResponseEntity);
        when(restTemplate.exchange(any(), eq(MapApiResultPath.class)))
                .thenReturn(new ResponseEntity<>(new MapApiResultPath(), HttpStatus.OK));
        // inject the mock RestTemplate into the service

        // when
        MapApiResultPath mapApiResultPath = mapApiService.callMapApi(request);

        // then
        verify(restTemplate, times(1)).exchange(any(), eq(MapApiResultXY.class));
        verify(restTemplate, times(1)).exchange(any(), eq(MapApiResultPath.class));
    }



}