package com.tickerBell.global.security.filter;

import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.security.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("필터 테스트")
    void doFilterInternalTest() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer accessToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Claims claims = Mockito.mock(Claims.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // stub
        when(jwtTokenProvider.isExpiration(any(String.class))).thenReturn(false);
        when(jwtTokenProvider.get(any(String.class))).thenReturn(claims);
        when(jwtTokenProvider.get("accessToken").get("username")).thenReturn("username");
        when(memberRepository.findByUsername("username")).thenReturn(Optional.of(Member.builder().username("username").password("pass").role(Role.ROLE_USER).build()));

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(jwtTokenProvider, times(1)).isExpiration(any(String.class));
        verify(memberRepository, times(1)).findByUsername("username");
    }
}
