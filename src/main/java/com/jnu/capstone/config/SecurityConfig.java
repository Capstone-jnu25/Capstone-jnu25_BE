package com.jnu.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // ✅ 이렇게 변경
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/users/signup",
//                                "/api/users/login",
//                                "/api/users/verify-email",  // ✅ 여기에 추가
//                                "/api/users/verify-code",
//                                "/api/schools"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher(matcher -> true) // 모든 요청 대상
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                .csrf(csrf -> csrf.disable()); // CSRF 비활성화
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());  // CSRF 비활성화

        return http.build();
    }
}
