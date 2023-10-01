package com.tickerBell.domain.event.entity;

public enum Category {
    MUSICAL("뮤지컬"), CONCERT("콘서트"), PLAY("공연"), CLASSIC("클래식"), SPORTS("스포츠");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
