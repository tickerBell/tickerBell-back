package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.*;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Long join(String username, String password, String phone, Boolean isAdult, Role role, AuthProvider authProvider);
    LoginResponse regenerateToken(RefreshTokenRequest refreshTokenRequest);
    LoginResponse login(String username, String password);
    JoinSmsValidationResponse joinSmsValidation(JoinSmsValidationRequest request);
    MyPageResponse getMyPage(Long memberId, Pageable pageable);
    MemberResponse getMember(Long memberId);
}
