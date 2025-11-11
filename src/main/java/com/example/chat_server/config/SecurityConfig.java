package com.example.chat_server.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity // 선택: 있어도/없어도 됨
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/loginPage", "/logout", "/noticeCheckPage", "/register", "/menu/all").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers("/resources/**", "/WEB-INF/**").permitAll()
                        .requestMatchers("/noticeAdd", "/noticeModifyPage").hasAnyAuthority("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/menu/add").hasAnyAuthority("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/menu/update").hasAnyAuthority("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/menu/delete").hasAnyAuthority("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/login")
                        .failureUrl("/loginPage?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    // 🔹 로그인 성공 시 세션 설정 및 리다이렉트 처리
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {

                // 세션 생성
                HttpSession session = request.getSession();

                // 권한이 ADMIN 또는 MANAGER인지 확인
                boolean isManager = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ADMIN") || auth.getAuthority().equals("MANAGER"));

                if (isManager) {
                    session.setAttribute("Manager", true);
                }

                // 로그인한 사용자 정보 세션 저장
                session.setAttribute("username", authentication.getName());
                session.setAttribute("isAuthenticated", true);

                // 로그인 성공 후 루트("/")로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    // 🔹 CORS 설정: React(3000) ↔ Spring(8080) 통신 허용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용 오리진 (프론트엔드 주소)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:8080",
                "https://localhost:8080"
        ));

        // 허용 메서드
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용 헤더
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN", "X-Requested-With"));

        // 브라우저에서 쿠키를 주고받을 수 있도록 허용
        configuration.setAllowCredentials(true);

        // 응답 헤더에 노출할 정보 (Set-Cookie 등)
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));

        // URL 패턴 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
