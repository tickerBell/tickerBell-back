package com.tickerBell.global.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledJob {
    private final SmsService smsService;
    private final TicketingRepository ticketingRepository;
    private final JobLauncher jobLauncher;
    private final Job SmsSendJob;

    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Seoul") // 초 분 시 일 월 요일
//    @Scheduled(fixedDelay = 30000)
    public void sendReminderMessages() {
        Map<String, JobParameter<?>> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis(), Long.class));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(SmsSendJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
        log.info("스케줄러 실행");
    }
}
