package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.KakaoUserInfo;
import com.tickerBell.domain.member.dtos.TokenRequest;
import com.tickerBell.domain.member.dtos.TokenResponse;
import com.tickerBell.global.security.dtos.LoginSuccessDto;

public interface OauthService {
    LoginSuccessDto redirect(TokenRequest tokenRequest);
    TokenResponse getToken(TokenRequest tokenRequest);
    KakaoUserInfo getUserInfo(String accessToken);
    TokenResponse getRefreshToken(String provider, String refreshToken);

}
