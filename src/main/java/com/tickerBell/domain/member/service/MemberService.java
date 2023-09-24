package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.RefreshTokenRequest;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.global.dto.Response;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    Long join(String username, String password, String phone, String email, Role role, AuthProvider authProvider);
    ResponseEntity<Response> regenerateToken(RefreshTokenRequest refreshTokenRequest);
}
