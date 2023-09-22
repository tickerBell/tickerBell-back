package com.tickerBell.global.security.context;

import com.tickerBell.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User {

    // CustomUserDetailsService 에서 UserDetails 를 리턴하기 위한 커스텀 클래스
    // User 객체는 UserDetails 인터페이스를 구현한다.
    // 향후 OAuth 사용 시 OAuth2User 인터페이스 구현하여 OAuth 사용 시 재사용 가능

    private final Member member;

    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
