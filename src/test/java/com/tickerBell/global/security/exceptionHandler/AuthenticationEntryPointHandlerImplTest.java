package com.tickerBell.global.security.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.global.dto.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationEntryPointHandlerImplTest {

    @InjectMocks
    private AuthenticationEntryPointHandlerImpl authenticationEntryPointHandler;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Test
    @DisplayName("AccessDeniedHandler 테스트")
    public void AccessDeniedTest() throws IOException, ServletException {
        AuthenticationException authException = new AuthenticationException("Authentication Required") {};


        // stub
        when(objectMapper.writeValueAsString(any(Response.class)))
                .thenReturn("{\"data\":\"null\", \"message\":\"접근이 거부되었습니다.\"}");
        // mock HttpServletResponse
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when
        authenticationEntryPointHandler.commence(request, response, authException);

        // then
        verify(writer, times(1)).write(any(String.class));
        verify(writer, times(1)).flush();
        verify(writer, times(1)).close();
    }
}