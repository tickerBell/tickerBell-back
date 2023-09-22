package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Role;

public interface MemberService {

    Long join(String username, String password, String phone, String email, Role role, AuthProvider authProvider);
}
