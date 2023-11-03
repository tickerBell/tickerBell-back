package com.tickerBell.domain.emitter.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class MemoryEmitterRepository implements EmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
    }

    public void deleteById(Long id) {
        emitters.remove(id);
    }

    public SseEmitter findById(Long id) {
        return emitters.get(id);
    }

    @Override
    public List<SseEmitter> findAll() {
        return emitters.values().stream().toList();
    }
}
