package com.tickerBell.domain.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tickerBell.domain.sms.dtos.MessageRequest;
import com.tickerBell.domain.sms.dtos.SendSmsResponse;
import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.global.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

//    @PostMapping("/sms/send")
//    public ResponseEntity<Response> sendSms(@RequestBody MessageRequest messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
//        SendSmsResponse sendSmsResponse = smsService.sendSms(messageDto.getTo(), messageDto.getContent());
//        return ResponseEntity.ok(new Response(sendSmsResponse, "문자 발송 완료"));
//    }
}