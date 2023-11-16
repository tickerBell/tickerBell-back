package com.tickerBell.domain.member.entity;

import com.tickerBell.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username; // 사용자 Id
    private String password; // 비밀번호
    private String phone; // 전화번호
    private Boolean isAdult; // 성인여부
    @Enumerated(EnumType.STRING)
    private Role role; // 권한
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider; // 인가서버

    @Builder
    public Member(String username, String password, String phone, Boolean isAdult, Role role, AuthProvider authProvider) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.isAdult = isAdult;
        this.role = role;
        this.authProvider = authProvider;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
