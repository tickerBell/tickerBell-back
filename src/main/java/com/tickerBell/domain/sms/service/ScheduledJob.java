package com.tickerBell.domain.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledJob {
    private final SmsService smsService;
    private final TicketingRepository ticketingRepository;

    // todo: 조회해서 문자 보내는 과정을 spring batch 로 변경
//    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Seoul") // 초 분 시 일 월 요일
    public void sendReminderMessages() throws UnsupportedEncodingException, ParseException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        log.info("스케줄러 실행");
        LocalDateTime tomorrow  = LocalDateTime.now().plusDays(1);

        // 00:00로 설정
        LocalDateTime tomorrowStart = tomorrow.withHour(0).withMinute(0);

        // 23:59로 설정
        LocalDateTime tomorrowEnd = tomorrow.withHour(23).withMinute(59);
        int tomorrowMonth = tomorrow.getMonthValue(); // 월(month) 추출
        int tomorrowDay = tomorrow.getDayOfMonth();   // 일(day) 추출

        log.info(tomorrowMonth + "월 " + tomorrowDay + "일 에 해당하는 " + "예매 내역 조회 후 예매자에게 문자 발송");

        // 다음 날 있는 예매 조회
        List<Ticketing> ticketingTomorrow = ticketingRepository.findTicketingTomorrow(tomorrowStart, tomorrowEnd);

        // 문자 발송
        for (Ticketing ticketing : ticketingTomorrow) {
            smsService.sendSms(ticketing.getMember().getPhone(), makeContent(ticketing));
        }
    }

    // sms content 생성
    private String makeContent(Ticketing ticketing) {
        String eventName = ticketing.getEvent().getName();
        LocalDateTime startEvent = ticketing.getEvent().getStartEvent();
        int month = startEvent.getMonthValue(); // 월(month) 추출
        int day = startEvent.getDayOfMonth();   // 일(day) 추출
        int hour = startEvent.getHour();        // 시(hour) 추출
        int minute = startEvent.getMinute();    // 분(minute) 추출
        String content = month + "월" + day + "일 " + hour + "시" + minute + "분에 " + eventName + "이 예매되어 있습니다. 시간에 맞춰 방문해주시기 바랍니다.";
        return content;
    }

}
