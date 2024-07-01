package com.sonic.sonic_backend.configuration.Security;

import com.sonic.sonic_backend.configuration.Auth.*;
import com.sonic.sonic_backend.domain.Member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
        return auth.build();
    }


    private final String[] WHITE_LIST = new String[]{
            "/swagger-ui/index.html"
            ,"/swagger-ui.html"
            ,"/swagger-ui/**"
            ,"/api-docs/**"
            ,"/v3/api-docs/**",
            "/auth/sign-up",
            "/auth/sign-in/general",
            "/auth/sign-in/kakao",
            "/auth/email",
            "/auth/password",
            "/error",
            "/auth/reissue",
            "/auth/send-mail",
            "/auth/password"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .httpBasic((httpBasic)->httpBasic.disable())
                .csrf((csrf)->csrf.disable())
                .cors((cors)->cors.disable())
                .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests)->
                        authorizeRequests
                                .requestMatchers(WHITE_LIST).permitAll()
                                .anyRequest().authenticated())
                                .exceptionHandling((exception) ->
                                        exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
