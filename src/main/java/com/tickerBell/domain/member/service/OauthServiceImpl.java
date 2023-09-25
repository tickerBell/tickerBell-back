package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.TokenRequest;
import com.tickerBell.domain.member.dtos.TokenResponse;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.repository.MemberRepository;
import com.tickerBell.global.dto.Response;
import com.tickerBell.global.security.dtos.LoginResponseDto;
import com.tickerBell.global.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthServiceImpl implements OauthService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI; // http://localhost:8080/login/oauth2/code/kakao

    @Value("${spring.security.oauth2.client.provider.kakao.token_uri}")
    private String TOKEN_URI; // https://kauth.kakao.com/oauth/token

    public ResponseEntity<Response> redirect(TokenRequest tokenRequest) {
        TokenResponse tokenResponse = getToken(tokenRequest); // WebClient 호출, 카카오에 accessToken 요청
        Map<String, Object> infoResponse = getUserInfo(tokenResponse.getAccessToken());
        log.info("카카오 api 호출 완료");

        Map<String, Object> kakaoAccountMap = (Map<String, Object>) infoResponse.get("kakao_account");
        Map<String, String> profileMap = (Map<String, String>) kakaoAccountMap.get("profile"); // 이름, 프로필
        Map<String, String> responseMap = new HashMap<>();

        // 닉네임 정보 담기
        if (StringUtils.hasText(profileMap.get("nickname"))) {
            responseMap.put("nickname", profileMap.get("nickname"));
        }
        // 프로필 사진 정보 담기
        if (StringUtils.hasText(profileMap.get("profile_image_url"))) {
            responseMap.put("profileImageUrl", profileMap.get("profile_image_url"));
        }
        // 이메일 정보 담기
        if ("true".equals(kakaoAccountMap.get("has_email").toString())) {
            responseMap.put("email", kakaoAccountMap.get("email").toString());
        }
        // 성별 정보 담기
        if ("true".equals(kakaoAccountMap.get("has_gender").toString())) {
            responseMap.put("gender", kakaoAccountMap.get("gender").toString());
        }
        // 연령대 정보 담기
        if ("true".equals(kakaoAccountMap.get("has_age_range").toString())) {
            responseMap.put("ageRange", kakaoAccountMap.get("age_range").toString());
        }
        // 생일 정보 담기
        if ("true".equals(kakaoAccountMap.get("has_birthday").toString())) {
            responseMap.put("birthday", kakaoAccountMap.get("birthday").toString());
        }

        // todo: 현재 email 이 필수항목은 아니지만 필수항목이라 가정
        Optional<Member> findMember = memberRepository.findByUsername(responseMap.get("email"));

        if (findMember.isPresent()) {
            String accessToken = jwtTokenProvider.createAccessToken(
                    String.valueOf(findMember.get().getUsername()));
            String refreshToken = jwtTokenProvider.createRefreshToken(
                    String.valueOf(findMember.get().getUsername()));

            LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                    .authProvider(AuthProvider.KAKAO)
                    .kakaoUserInfo(null)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            // 로그인 완료 시 토큰과 함께 반환
            return ResponseEntity.ok(new Response(loginResponseDto, "카카오 로그인 성공"));
        }
        // 회원이 아닐 경우 카카오 유저 정보만 반환. 이 정보로 회원가입 창으로 이동.
        else {
            log.info("가입되지 않은 Oauth 요청");
            return ResponseEntity.ok(new Response(responseMap, "회원가입하지 않은 회원의 Oauth 접근. 카카오 유저 정보 반환"));
        }
    }


    public TokenResponse getToken(TokenRequest tokenRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("redirect_uri", REDIRECT_URI);
        formData.add("client_id", CLIENT_ID);
        formData.add("code", tokenRequest.getCode());

        return webClient.mutate()
                .baseUrl(TOKEN_URI)
                .build()
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData)) //요청 본문을 설정
                .retrieve() // 요청을 보내고, 서버로부터 응답 받음. Mono 형태의 응답
//                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new BadRequestException()))
                .bodyToMono(TokenResponse.class) // TokenResponse 클래스로 변환
                .block(); // 비동기적 요청 후, 응답을 블로킹하여 대기. 최종적으로 TokenResponse 객체를 반환
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        /**
         * accessToken으로 로그인 사용자가 동의한 정보 확인하기
         */
        // webClient 설정
        WebClient kakaoApiWebClient =
                WebClient.builder()
                        .baseUrl("https://kapi.kakao.com")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();

        // info api 설정
        Map<String, Object> infoResponse =
                kakaoApiWebClient
                        .post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v2/user/me")
                                .build())
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
        return infoResponse;
    }

    public TokenResponse getRefreshToken(String provider, String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", CLIENT_ID);
        formData.add("refresh_token", refreshToken);

        return webClient.mutate()
                .baseUrl("https://kauth.kakao.com")
                .build()
                .post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
