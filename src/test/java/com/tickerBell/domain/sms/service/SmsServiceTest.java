package com.tickerBell.domain.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tickerBell.domain.sms.dtos.MessageRequest;
import com.tickerBell.domain.sms.dtos.SendSmsResponse;
import com.tickerBell.domain.sms.dtos.SmsRequest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @InjectMocks
    private SmsService smsService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(smsService, "accessKey", "mock_accessKey");
        ReflectionTestUtils.setField(smsService, "secretKey", "mock_secretKey");
        ReflectionTestUtils.setField(smsService, "serviceId", "mock_serviceId");
        ReflectionTestUtils.setField(smsService, "phone", "mock_phone");
    }

    @Test
    @DisplayName("문자 전송 테스트")
    public void sendSmsTest() throws UnsupportedEncodingException, ParseException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        System.out.println();
        // given
        String recipientPhoneNumber = "123456789";
        String content = "Test Message";

        // stub
        when(restTemplate.postForObject(any(URI.class), any(HttpEntity.class), any()))
                .thenReturn(SendSmsResponse.builder().build());

        // when
        SendSmsResponse sendSmsResponse = smsService.sendSms(recipientPhoneNumber, content);

        // then
        verify(restTemplate, times(1)).postForObject(any(URI.class), any(HttpEntity.class), any());
    }

}
