package com.tickerBell.global.batch;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.sms.service.SmsService;
import com.tickerBell.domain.ticketing.entity.Ticketing;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SmsJobConfiguration {
    private final SmsService smsService;
    private final EntityManagerFactory entityManagerFactory;
    @Bean @Qualifier(value = "smsSendJob")
    public Job smsSendJob(JobRepository jobRepository, Step smsSendStep) {
        return new JobBuilder("smsSendJob", jobRepository)
                .start(smsSendStep)
                .build();
    }
    @Bean
    public Step smsSendStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("smsSendStep", jobRepository)
                .<Ticketing, Ticketing>chunk(5, platformTransactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<Ticketing> itemReader() {
        LocalDateTime tomorrow  = LocalDateTime.now().plusDays(1);

        // DateTimeFormatter를 사용하여 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 00:00로 설정
        LocalDateTime tomorrowStart = tomorrow.withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 23:59로 설정
        LocalDateTime tomorrowEnd = tomorrow.withHour(23).withMinute(59);

        return new JpaCursorItemReaderBuilder<Ticketing>()
                .name("itemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select t from Ticketing t join fetch t.member m where t.event.startEvent between :tomorrowStart and :tomorrowEnd")
                .parameterValues(Map.of("tomorrowStart", tomorrowStart, "tomorrowEnd", tomorrowEnd))
                .build();
    }

    @Bean
    public ItemProcessor<Ticketing, Ticketing> itemProcessor() {
        return ticketing -> {
            Member member = ticketing.getMember();
            String eventName = ticketing.getEvent().getName();
            LocalDateTime startEvent = ticketing.getEvent().getStartEvent();

            int month = startEvent.getMonthValue(); // 월(month) 추출
            int day = startEvent.getDayOfMonth();   // 일(day) 추출
            int hour = startEvent.getHour();        // 시(hour) 추출
            int minute = startEvent.getMinute();    // 분(minute) 추출
            String content = month + "월" + day + "일 " + hour + "시" + minute + "분에 " + eventName + "이 예매되어 있습니다. 시간에 맞춰 방문해주시기 바랍니다.";
            log.info(member.getPhone() + " 해당 전화번호로 [" + content + "] 문자 발송되었습니다.");
            // todo: 요금때문에 실제로 send 로직은 주석 처리
//            smsService.sendSms(member.getPhone(), content);
            return ticketing;
        };
    }

    @Bean
    public JpaItemWriter<Ticketing> itemWriter() {
        return new JpaItemWriterBuilder<Ticketing>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}