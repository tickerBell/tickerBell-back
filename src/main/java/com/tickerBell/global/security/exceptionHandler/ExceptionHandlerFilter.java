package com.tickerBell.global.security.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.global.dto.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletRequest request,
                                 HttpServletResponse response, Throwable e) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(new Response(e.getMessage())));
    }
}
