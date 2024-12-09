package com.kosa.moimeasy.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화
        http.csrf(csrf -> csrf.disable());


        // URL 접근 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/**").permitAll()
//                .requestMatchers("/cart", "/mypage").authenticated()
                .anyRequest().authenticated()
        );

        http.formLogin((formLogin) -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
//                .defaultSuccessUrl("/main")
                        .failureUrl("/auth/login?error")
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/*.html", "/html/**");
    }
}