package com.tickerBell.global.security.dtos;

import com.tickerBell.domain.member.dtos.KakaoUserInfo;
import com.tickerBell.domain.member.entity.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginSuccessDto {

    private String accessToken;
    private String refreshToken;

    // oauth 로그인 시
    private AuthProvider authProvider;
    private KakaoUserInfo kakaoUserInfo;
}
