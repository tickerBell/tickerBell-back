package com.tickerBell.domain.category.entity;

public enum Categories {
    MUSICAL("뮤지컬"), CONCERT("콘서트"), PLAY("공연"), CLASSIC("클래식"), SPORTS("스포츠");

    private final String description;

    Categories(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
