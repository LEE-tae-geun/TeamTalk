package com.example.chat_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;   // 🔽 추가
import org.springframework.security.crypto.password.PasswordEncoder;   // 🔽 추가
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // ✅ 회원가입 요청(승인 전 로그인 불가)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/signup-requests").permitAll()

                        // (기존) 로그인/로그아웃은 그대로 허용
                        .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()

                        // (기존) /api/auth/register는 남겨두되, 실제 운영은 signup-requests 플로우 권장
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/register").permitAll()

                        // ✅ 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 나머지 API는 로그인 필요
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable());

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}