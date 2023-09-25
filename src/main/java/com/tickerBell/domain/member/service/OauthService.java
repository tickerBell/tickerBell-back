package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.TokenRequest;
import com.tickerBell.domain.member.dtos.TokenResponse;
import com.tickerBell.global.dto.Response;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OauthService {
    ResponseEntity<Response> redirect(TokenRequest tokenRequest);
    TokenResponse getToken(TokenRequest tokenRequest);
    Map<String, Object> getUserInfo(String accessToken);
    TokenResponse getRefreshToken(String provider, String refreshToken);

}
