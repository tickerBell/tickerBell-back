package com.tickerBell.domain.member.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfo {
    private Long id; // 카카오 서비스에서 관리하는 고유 아이디
    private KakaoAccount kakaoAccount; // 카카오 계정 정보: 이메일, 프로필

    @Getter
    private static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        private static class Profile {
            private String nickname;
            private String profileImageUrl;
        }
    }
}
