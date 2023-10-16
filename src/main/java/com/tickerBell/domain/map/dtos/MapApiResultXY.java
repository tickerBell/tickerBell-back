package com.tickerBell.domain.map.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapApiResultXY {
    private String status;
    private Meta meta;
    private List<Address> addresses;
    private String errorMessage;

    @Getter
    @Setter
    public static class Meta {
        private int totalCount;
        private int page;
        private int count;
    }

    @Getter
    @Setter
    public static class Address {
        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
        private List<AddressElement> addressElements;
        private String x;
        private String y;
        private double distance;
    }

    @Getter
    @Setter
    public static class AddressElement {
        private List<String> types;
        private String longName;
        private String shortName;
        private String code;
    }
}