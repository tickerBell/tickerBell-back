package com.tickerBell.domain.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.tickerBell.domain.sms.dtos.MessageRequest;
import com.tickerBell.domain.sms.dtos.SendSmsResponse;
import com.tickerBell.domain.sms.dtos.SmsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {
    private final RestTemplate restTemplate;

    /**
     * 참고 블로그
     * https://born2bedeveloper.tistory.com/67 (sens 세팅, 프로퍼티 해당 블로그 참고)
     * https://koogood.tistory.com/27 (코드 해당 블로그 참고)
     */
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public SendSmsResponse sendSms(String recipientPhoneNumber, String content) throws ParseException, JsonProcessingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        Long time = System.currentTimeMillis();
        List<MessageRequest> messages = new ArrayList<>();
        // 수신자 전화번호와 내용
        messages.add(new MessageRequest(recipientPhoneNumber,content)); // content부분이 내용임

        // SMS 타입, 컨텐츠 타입, 국가 코드, 발신자 전화번호, (?), 메시지 목록을 전달
        SmsRequest smsRequestDto = new SmsRequest("SMS", "COMM", "82", this.phone, content, messages);

        // json 형태로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequestDto);

        // 헤더에서 여러 설정값들을 잡아준다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);

        // signature 서명하기.
        String sig = makeSignature(time);
        headers.set("x-ncp-apigw-signature-v2", sig);

        // 위에서 조립한 jsonBody와 헤더를 조립한다.
        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
        System.out.println(body.getBody());

        // restTemplate로 post 요청을 보낸다. 별 일 없으면 202 코드 반환된다.
        SendSmsResponse sendSmsResponseDto = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SendSmsResponse.class);
        System.out.println(sendSmsResponseDto.getStatusCode());
        return sendSmsResponseDto;
    }

    public String makeSignature(Long time) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/"+ this.serviceId +"/messages";	// 요청 url
        String timestamp = time.toString();			// 현재 시간
        String accessKey = this.accessKey;			// 액세스 키
        String secretKey = this.secretKey;          // 시크릿 키

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        // HmacSHA256 알고리즘을 사용하여 서명을 생성.
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

        // 서명 인코딩 후 반환
        String encodeBase64String = Base64.encodeBase64String(rawHmac);
        return encodeBase64String;
    }
}
