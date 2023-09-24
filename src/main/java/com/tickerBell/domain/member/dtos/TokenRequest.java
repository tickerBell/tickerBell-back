package com.tickerBell.domain.member.dtos;

import lombok.Builder;
import lombok.Getter;

/**
 * 카카오 api 호출을 위해 필요한 정보 dto
 */
@Getter
@Builder
public class TokenRequest {
    private String registrationId; // "google", "kakao" 등 어떤 프로바이더인지
    private String code; // 토큰을 교환하기 위해 사용
    private String state;
    private String refreshToken;
}
