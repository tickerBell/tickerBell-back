package com.tickerBell.global.batch;

import com.tickerBell.domain.alarm.dtos.SaveAlarmRequest;
import com.tickerBell.domain.alarm.service.AlarmService;
import com.tickerBell.domain.emitter.service.EmitterService;
import com.tickerBell.domain.event.entity.Event;
import com.tickerBell.domain.member.entity.Member;
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
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AlarmJobConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final AlarmService alarmService;
    private final EmitterService emitterService;

    @Bean
    @Qualifier("alarmJob")
    public Job alarmJob(JobRepository jobRepository, Step alarmStep) {
        return new JobBuilder("alarmJob", jobRepository)
                .start(alarmStep)
                .build();
    }

    @Bean
    public Step alarmStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("alarmJob", jobRepository)
                .<Event, Event> chunk(5, platformTransactionManager)
                .reader(eventItemReader())
                .processor(eventItemProcessor())
                .writer(eventItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Event> eventItemReader() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime todayStart = today.withHour(0).withMinute(0);
        String todayStartFormat = todayStart.format(formatter);

        LocalDateTime after3Days = LocalDateTime.now().plusDays(3).withHour(23).withMinute(59);
        String after3DaysFormat = after3Days.format(formatter);

        return new JpaCursorItemReaderBuilder<Event>()
                .name("eventReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select e from Event e join fetch e.member em where e.startEvent between :today and :after")
                .parameterValues(Map.of("today", todayStartFormat, "after", after3DaysFormat))
                .build();
    }

    @Bean
    public ItemProcessor<Event, Event> eventItemProcessor() {
        return event -> {
            Member member = event.getMember();
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

            return event;
        };
    }

    private String makeMessage(String name, Long hour, Long minute) {
        return "예매하신 " + name + "가 "
                + hour + " 시간 "
                + minute + " 분 "
                + "남았습니다.";
    }

    @Bean
    public JpaItemWriter<Event> eventItemWriter() {
        return new JpaItemWriterBuilder<Event>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
