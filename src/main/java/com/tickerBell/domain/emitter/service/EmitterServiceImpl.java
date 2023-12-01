package com.tickerBell.domain.emitter.service;

import com.tickerBell.domain.emitter.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterServiceImpl implements EmitterService{
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;

    @Override
    public SseEmitter subscribe(Long memberId) {
        SseEmitter sseEmitter = createEmitter(memberId);

        sendToClient(memberId, "EventStream Created. [memberId = " + memberId + "]");
        return sseEmitter;
    }

    @Override
    public void notify(Long memberId, Object event) {
        sendToClient(memberId, event);
    }

    private SseEmitter createEmitter(Long memberId) {

        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memberId, sseEmitter);

        log.info("in createEmitter");
        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion");
            emitterRepository.deleteById(memberId);
            log.info("onCompletionAfter");
        });
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        sseEmitter.onTimeout(() -> {
            log.info("onTimeout");
            emitterRepository.deleteById(memberId);
            log.info("onTimeoutAfter");
        });
        log.info("in createEmitterAfter");

        return sseEmitter;
    }

    private void sendToClient(Long memberId, Object data) {
        SseEmitter findSseEmitter = emitterRepository.findById(memberId);

        if (findSseEmitter != null) {
            try {
                log.info("in sendToClient");
                findSseEmitter.send(SseEmitter.event().id(String.valueOf(memberId)).name("sse").data(data));
                log.info("in sendToClientSendAfter");
            } catch (IOException e) {
                emitterRepository.deleteById(memberId);
                findSseEmitter.completeWithError(e);
            }
        }
    }
}
