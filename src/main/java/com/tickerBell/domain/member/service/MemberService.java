package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.global.security.dtos.LoginResponseDto;

public interface MemberService {

    Long join(String username, String password, String phone, String email, Role role, AuthProvider authProvider);
    LoginResponseDto regenerateToken(RefreshTokenRequest refreshTokenRequest);
    LoginResponseDto login(String username, String password);
}
