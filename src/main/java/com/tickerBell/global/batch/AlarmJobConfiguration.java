package com.tickerBell.global.batch;

import com.tickerBell.domain.alarm.dtos.SaveAlarmRequest;
import com.tickerBell.domain.alarm.service.AlarmService;
import com.tickerBell.domain.emitter.service.EmitterService;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AlarmJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final AlarmService alarmService;
    private final EmitterService emitterService;

    @Bean @Qualifier(value = "alarmJob")
    public Job alarmJob(JobRepository jobRepository, Step alarmStep) {
        return new JobBuilder("alarmJob", jobRepository)
                .start(alarmStep)
                .build();
    }

    @Bean
    public Step alarmStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("alarmJob", jobRepository)
                .<Ticketing, Ticketing> chunk(5, platformTransactionManager)
                .reader(alarmItemReader())
                .processor(alarmItemProcessor())
                .writer(alarmItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Ticketing> alarmItemReader() {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayStart = today.withHour(0).withMinute(0);

        LocalDateTime after3Days = LocalDateTime.now().plusDays(3).withHour(23).withMinute(59);

        return new JpaCursorItemReaderBuilder<Ticketing>()
                .name("alarmItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select t from Ticketing t join fetch t.event te join fetch t.member tm where te.startEvent between :today and :after")
                .parameterValues(Map.of("today", todayStart, "after", after3Days))
                .build();
    }

    @Bean
    public ItemProcessor<Ticketing, Ticketing> alarmItemProcessor() {
        return ticketing -> {
            Member member = ticketing.getMember();
            Event event = ticketing.getEvent();
            LocalDateTime startEvent = event.getStartEvent();
            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(now, startEvent);
            Long hoursRemaining = duration.toHours(); // 남은 시간
            Long minutesRemaining = duration.toMinutes() % 60; // 분

            String message = makeMessage(event.getName(), hoursRemaining, minutesRemaining);

            emitterService.notify(member.getId(), message);
            SaveAlarmRequest saveAlarmRequest = new SaveAlarmRequest();
            saveAlarmRequest.setMemberId(member.getId());
            saveAlarmRequest.setMessage(message);
            alarmService.saveAlarm(saveAlarmRequest);

            return ticketing;
        };
    }

    private String makeMessage(String name, Long hour, Long minute) {
        return "예매하신 " + name + "가 "
                + hour + " 시간 "
                + minute + " 분 "
                + "남았습니다.";
    }

    @Bean
    public JpaItemWriter<Ticketing> alarmItemWriter() {
        return new JpaItemWriterBuilder<Ticketing>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
