package com.tickerBell.global.batch;

import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.domain.ticketing.repository.TicketingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ScheduledJob {
    private final SmsService smsService;
    private final TicketingRepository ticketingRepository;
    private final JobLauncher jobLauncher;
    private final Job SmsSendJob;
    private final Job alarmJob;

    public ScheduledJob(SmsService smsService, TicketingRepository ticketingRepository, JobLauncher jobLauncher, @Qualifier("smsSendJob") Job smsSendJob, @Qualifier("alarmJob") Job alarmJob) {
        this.smsService = smsService;
        this.ticketingRepository = ticketingRepository;
        this.jobLauncher = jobLauncher;
        SmsSendJob = smsSendJob;
        this.alarmJob = alarmJob;
    }

    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Seoul") // 초 분 시 일 월 요일
//    @Scheduled(fixedDelay = 30000)
    public void sendReminderMessages() {
        Map<String, JobParameter<?>> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis(), Long.class));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(SmsSendJob, jobParameters);
            jobLauncher.run(alarmJob, jobParameters);
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
