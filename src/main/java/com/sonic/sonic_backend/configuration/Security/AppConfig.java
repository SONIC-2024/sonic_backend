package com.sonic.sonic_backend.configuration.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    // CustomAuthenticationProvider && SecurityConfig 의 bean 순환참조 방지
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
