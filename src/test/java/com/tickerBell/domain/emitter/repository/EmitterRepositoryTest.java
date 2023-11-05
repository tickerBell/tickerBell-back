package com.tickerBell.domain.emitter.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@ContextConfiguration(classes = MemoryEmitterRepository.class)
public class EmitterRepositoryTest {

    @Autowired
    private EmitterRepository emitterRepository;

    @Test
    void saveTest() {
        // given
        Long id = 1L;
        SseEmitter sseEmitter = new SseEmitter();

        // when
        emitterRepository.save(id, sseEmitter);

        // then
        SseEmitter findSseEmitter = emitterRepository.findById(id);
        assertThat(findSseEmitter).isEqualTo(sseEmitter);
    }

    @Test
    void deleteByIdTest() {
        // given
        Long id = 1L;
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save(id, sseEmitter);

        // when
        emitterRepository.deleteById(id);

        // then
        SseEmitter findSseEmitter = emitterRepository.findById(id);
        assertThat(findSseEmitter).isNull();
    }

    @Test
    void findByIdTest() {
        // given
        Long id = 1L;
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save(id, sseEmitter);

        // when
        SseEmitter findSseEmitter = emitterRepository.findById(id);

        // then
        assertThat(findSseEmitter).isEqualTo(sseEmitter);
    }

    @Test
    void findAllTest() {
        // given
        Long id1 = 1L;
        Long id2 = 2L;
        SseEmitter sseEmitter1 = new SseEmitter();
        SseEmitter sseEmitter2 = new SseEmitter();
        emitterRepository.save(id1, sseEmitter1);
        emitterRepository.save(id2, sseEmitter2);

        // when
        List<SseEmitter> findSseEmitters = emitterRepository.findAll();

        // then
        assertThat(findSseEmitters.size()).isEqualTo(2);
    }
}