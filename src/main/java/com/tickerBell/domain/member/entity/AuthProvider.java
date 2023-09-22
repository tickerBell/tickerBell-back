package com.tickerBell.domain.member.entity;

public enum AuthProvider {
    KAKAO("카카오 로그인");

    private String description;

    AuthProvider(String description) {
        this.description = description;
    }
}
