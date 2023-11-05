package com.tickerBell.domain.emitter.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface EmitterRepository {
    void save(Long id, SseEmitter sseEmitter);
    void deleteById(Long id);
    SseEmitter findById(Long id);
    List<SseEmitter> findAll();
}
