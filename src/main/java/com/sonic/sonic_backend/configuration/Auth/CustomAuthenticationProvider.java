package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.exception.LogInNotMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        System.out.println("in customAuthenticationProvider");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // password 일치하지 않으면!
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new LogInNotMatch();
        }
        System.out.println(userDetails.getAuthorities());

        Authentication authenticationToken
                = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities());
        return authenticationToken;
    }

    // 토큰 타입과 일치할 때 인증
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}