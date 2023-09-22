package com.tickerBell.global.test;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Operation(summary = "test", description = "test description")
    @PostMapping("/test")
    public String test(@RequestBody TestDto testDto) {
        return "test";
    }
}
