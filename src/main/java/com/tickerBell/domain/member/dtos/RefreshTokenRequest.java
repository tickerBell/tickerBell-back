package com.tickerBell.domain.member.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class RefreshTokenRequest {
    @Schema(description = "refresh token", example = "refresh_token")
    private String refreshToken;
}
