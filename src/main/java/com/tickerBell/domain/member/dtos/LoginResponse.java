package com.tickerBell.domain.member.dtos;

import com.tickerBell.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Role role; // 권한
}
