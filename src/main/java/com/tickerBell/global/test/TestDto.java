package com.tickerBell.global.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TestDto {
    @Schema(description = "a", example = "aaa")
    private String a;
    @Schema(description = "b", example = "bbb")
    private String b;
    @Schema(description = "c", example = "ccc")
    private String c;
}
