package com.tickerBell.domain.member.service;

import com.tickerBell.domain.member.entity.AuthProvider;
import com.tickerBell.domain.member.entity.Member;
import com.tickerBell.domain.member.entity.Role;
import com.tickerBell.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long join(String username, String password, String phone, String email, Role role, AuthProvider authProvider) {

        if (checkUsername(username) == false) {
            throw new IllegalArgumentException("Duplicated Username");
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .email(email)
                .role(role)
                .authProvider(authProvider)
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    private boolean checkUsername(String username) {
        Member findUsername = memberRepository.findByUsername(username);

        if (findUsername != null) {
            return false;
        }
        return true;
    }
}
