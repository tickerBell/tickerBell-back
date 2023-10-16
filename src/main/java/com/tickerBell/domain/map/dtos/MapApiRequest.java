package com.tickerBell.domain.map.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MapApiRequest {
    @Schema(description = "목적지 주소", example = "성수동 11-1")
    private String query;
    @Schema(description = "출발지 위도,경도", example = "126.9652628,37.4750974")
    private String start;
}
