package com.tickerBell.global.security.config;

import com.tickerBell.global.security.exceptionHandler.AccessDeniedHandlerImpl;
import com.tickerBell.global.security.exceptionHandler.AuthenticationEntryPointHandlerImpl;
import com.tickerBell.global.security.filter.JwtFilter;
import com.tickerBell.global.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointHandlerImpl authenticationEntryPointHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //https://mingeonho1.tistory.com/entry/Spring-Security-%EB%B2%84%EC%A0%84-%EC%9D%B4%EC%8A%88%EB%A1%9C-%EC%9D%B8%ED%95%9C-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors() // cors 커스텀 설정
                .and()

                .sessionManagement()//세션 사용 x
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeHttpRequests()
                .requestMatchers("/api/member").hasAnyRole("USER", "REGISTRANT")
                .requestMatchers("/api/event", "/api/image").hasRole("REGISTRANT") // 등록자 권한 설정
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/api/event/**", "/api/members", "/reissue", "/api/login", "/api/join/sms-validation").permitAll()
                .requestMatchers(HttpMethod.POST, "/reissue", "/api/members", "/api/login",
                        "/api/join/sms-validation", "/naver-api/path", "/ticketing-nonMember",
                        "/graphql").permitAll()
                .requestMatchers(HttpMethod.GET, "/ticketing-nonMember", "/api/main", "/api/events/{category}",
                        "/api/event/{eventId}", "/login/oauth2/code", "/api/events", "/selected-seat/{eventId}",
                        "/error").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/ticketing-nonMember/{ticketingId}").permitAll()
                .anyRequest().authenticated()
                .and()

                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/code/*")
        ;

        http
                .userDetailsService(customUserDetailsService);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler) // 커스텀 AccessDeniedHandler 등록
                .authenticationEntryPoint(authenticationEntryPointHandler); // 커스텀 AuthenticationEntryPoint 등록

        return http.build();
    }
}
