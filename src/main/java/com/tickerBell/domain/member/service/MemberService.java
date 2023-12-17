package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.dtos.*;
import com.tickerBell.domain.member.dtos.join.JoinSmsValidationRequest;
import com.tickerBell.domain.member.dtos.join.JoinSmsValidationResponse;
import com.tickerBell.domain.member.dtos.login.LoginResponse;
import com.tickerBell.domain.member.dtos.login.RefreshTokenRequest;
import com.tickerBell.domain.member.dtos.myPage.MyPageResponse_V2;
import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Long join(String username, String password, String phone, Boolean isAdult, Role role, AuthProvider authProvider);
    LoginResponse regenerateToken(RefreshTokenRequest refreshTokenRequest);
    LoginResponse login(String username, String password);
    JoinSmsValidationResponse joinSmsValidation(JoinSmsValidationRequest request);
    MyPageResponse_V2 getMyPage_(Long memberId, PageRequest pageRequest);
    MemberResponse getMember(Long memberId);
    MemberResponse getMemberByUsername(String username);
    void updatePassword(Long memberId, String password);
    Boolean checkCurrentPassword(Long memberId, String password);
}
