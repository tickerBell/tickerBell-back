package com.tickerBell.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.global.security.context.MemberContext;
import com.tickerBell.global.security.dtos.LoginDto;
import com.tickerBell.global.security.dtos.LoginResponseDto;
import com.tickerBell.global.security.token.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 일반 회원 (Oauth x)
     */
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        // /api/login url 로 요청 시에만 필터 작동
        super(new AntPathRequestMatcher("/api/login"));
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = null;

        // 로그인 요청 데이터 -> loginDto 로 변환
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // authentication token 생성 후 UserDetailsService 호출
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // 로그인 성공 시 실행 메서드
        MemberContext memberContext = (MemberContext) authResult.getPrincipal();
        Member loginMember = memberContext.getMember();

        String username = loginMember.getUsername();
        Role role = loginMember.getRole();

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(username, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(username, role);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);
        String loginSuccess = objectMapper.writeValueAsString(loginResponseDto);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().println(loginSuccess);
    }
}
