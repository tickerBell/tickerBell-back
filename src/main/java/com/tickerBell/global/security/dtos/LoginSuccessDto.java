package com.tickerBell.global.security.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginSuccessDto {

    private String accessToken;
    private String refreshToken;
}
