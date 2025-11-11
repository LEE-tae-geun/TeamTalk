package com.example.chat_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    // 로그인 상태 확인용 API
    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication auth) {
        if (auth == null) {
            return Map.of("authenticated", false);
        }

        return Map.of(
                "authenticated", true,
                "username", auth.getName(),
                "roles", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
