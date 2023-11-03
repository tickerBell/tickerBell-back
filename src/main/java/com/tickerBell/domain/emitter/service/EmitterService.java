package com.tickerBell.domain.emitter.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterService {
    SseEmitter subscribe(Long memberId);

    void notify(Long memberId, Object event);
}
