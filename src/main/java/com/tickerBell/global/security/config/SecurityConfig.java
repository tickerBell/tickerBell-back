package com.tickerBell.global.security.config;

import com.tickerBell.global.security.filter.JwtFilter;
import com.tickerBell.global.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .requestMatchers("/api/event").hasRole("REGISTRANT") // 등록자 권한 설정
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/**").permitAll()
                .and()

                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/code/*")
        ;

        http
                .userDetailsService(customUserDetailsService);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
