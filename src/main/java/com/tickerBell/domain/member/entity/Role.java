package com.tickerBell.domain.member.entity;

public enum Role {
    ROLE_USER("일반 사용자"), ROLE_REGISTRANT("이벤트 등록자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
