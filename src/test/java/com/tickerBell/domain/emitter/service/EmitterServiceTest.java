package com.tickerBell.domain.emitter.service;

import com.tickerBell.domain.emitter.repository.EmitterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmitterServiceTest {

    @InjectMocks
    private EmitterServiceImpl emitterService;
    @Mock
    private EmitterRepository emitterRepository;

    @Test
    void subscribeTest() {
        // given
        Long memberId = 1L;
        SseEmitter sseEmitter = new SseEmitter();

        // stub
        doNothing().when(emitterRepository).save(anyLong(), any(SseEmitter.class));
        when(emitterRepository.findById(memberId)).thenReturn(sseEmitter);

        // when
        SseEmitter subscribe = emitterService.subscribe(memberId);

        // then
        verify(emitterRepository, times(1)).save(anyLong(), any(SseEmitter.class));
        verify(emitterRepository, times(1)).findById(memberId);
        Assertions.assertThat(subscribe).isExactlyInstanceOf(SseEmitter.class);
    }

    @Test
    void subscribeFindFailTest() {
        // given
        Long memberId = 1L;
        SseEmitter sseEmitter = new SseEmitter();

        // stub
        doNothing().when(emitterRepository).save(anyLong(), any(SseEmitter.class));
        when(emitterRepository.findById(memberId)).thenReturn(null);

        // when
        SseEmitter subscribe = emitterService.subscribe(memberId);

        // then
        verify(emitterRepository, times(1)).save(anyLong(), any(SseEmitter.class));
        verify(emitterRepository, times(1)).findById(memberId);
        verifyNoMoreInteractions(emitterRepository);
    }

    @Test
    void subscribeExceptionTest() throws IOException {
        // given
        Long memberId = 1L;
        SseEmitter mockSseEmitter = mock(SseEmitter.class);

        // stub
        doNothing().when(emitterRepository).save(anyLong(), any(SseEmitter.class));
        when(emitterRepository.findById(memberId)).thenReturn(mockSseEmitter);
        doThrow(new IOException()).when(mockSseEmitter).send(any());

        // when
        emitterService.subscribe(memberId);

        // then
        verify(emitterRepository, times(1)).save(anyLong(), any(SseEmitter.class));
        verify(emitterRepository, times(1)).findById(memberId);
        verify(emitterRepository, times(1)).deleteById(memberId);
    }

    @Test
    void notifyTest() {
        // given
        Long memberId = 1L;
        String event = "event";

        // stub
        when(emitterRepository.findById(memberId)).thenReturn(any(SseEmitter.class));

        // when
        emitterService.notify(memberId, event);

        // then
        verify(emitterRepository, times(1)).findById(memberId);
        verifyNoMoreInteractions(emitterRepository);
    }
}
