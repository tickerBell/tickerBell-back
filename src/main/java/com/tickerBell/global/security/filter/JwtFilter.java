package com.tickerBell.global.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;
import com.tickerBell.global.security.context.MemberContext;
import com.tickerBell.global.security.token.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            String username;
            // 헤더가 null 이 아니고 올바른 토큰이라면
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && !shouldExcludeUrl(request.getRequestURI())) {
                // 토큰 추출
                token = authorizationHeader.substring(7);
                // 만료 체크
                if (jwtProvider.isExpiration(token)) {
                    log.info("access token 만료");
                    throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
                }

                // claim 을 받아와 정보 추출
                username = (String) jwtProvider.get(token).get("username");

                // DB 에 정보가 있는지 확인
                Member member = memberRepository.findByUsername(username)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                List<GrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(member.getRole().name()));

                MemberContext memberContext = new MemberContext(member, roles);

                // 인증 정보 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(memberContext, null, memberContext.getAuthorities());
                // SecurityContextHolder에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("회원 인증 완료");
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // 만료된 토큰 에러라면
            if (e.getMessage().equalsIgnoreCase("만료된 access token 입니다.")) {
                writeErrorLogs("EXPIRED_ACCESS_TOKEN", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            }
            // DB 에 없는 유저라면
            else if (e.getMessage().equalsIgnoreCase("회원이 존재하지 않습니다.")) {
                writeErrorLogs("CANNOT_FOUND_USER", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            }
        }
//        } catch (Exception e) {
//            writeErrorLogs("Exception", e.getMessage(), e.getStackTrace());
//
//            // 반환 데이터 인코딩 처리
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json;charset=UTF-8"); // JSON 응답을 UTF-8로 설정
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setContentType(APPLICATION_JSON_VALUE);
//
//            response.getWriter().write(objectMapper.writeValueAsString(new Response(e.getMessage())));
//            response.getWriter().flush();
//            response.getWriter().close();
//
//            if (response.getStatus() == HttpStatus.OK.value()) {
//                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            }
//        } finally {
//            log.debug("**** SECURITY FILTER FINISH");
//        }
    }

    // 에러 content
    private JSONObject createJsonError(String errorCode, String errorMessage) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("error_code", errorCode);
            jsonObject.put("error_message", errorMessage);
        } catch (Exception ex) {
            writeErrorLogs("JSONException", ex.getMessage(), ex.getStackTrace());
        }

        return jsonObject;
    }

    // 에러 응답 반환
    private void setJsonResponse(HttpServletResponse response, HttpStatus httpStatus, String jsonValue) {
        // 반환 데이터 인코딩 처리
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8"); // JSON 응답을 UTF-8로 설정

        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        try {
            response.getWriter().write(jsonValue);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ex) {
            writeErrorLogs("IOException", ex.getMessage(), ex.getStackTrace());
        }
    }

    private void writeErrorLogs(String exception, String message, StackTraceElement[] stackTraceElements) {
        log.error("**** " + exception + " ****");
        log.error("**** error message : " + message);
        log.error("**** stack trace : " + Arrays.toString(stackTraceElements));
    }

    private boolean shouldExcludeUrl(String requestUri) {
        return requestUri.startsWith("/api/main") || requestUri.startsWith("/reissue");
    }
}
