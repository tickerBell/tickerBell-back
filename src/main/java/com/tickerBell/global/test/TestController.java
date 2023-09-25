package com.tickerBell.global.test;

import com.tickerBell.global.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Operation(summary = "test", description = "test description")
    @GetMapping("/test")
    public String test() {
        return "login";
    }

    @PostMapping("/test2")
    @ResponseBody
    public ResponseEntity<Response> tt(@AuthenticationPrincipal User user) {
        log.info("id:  " + user.getUsername());
        return ResponseEntity.ok(new Response(user.getUsername(), "gd"));

    }
}
