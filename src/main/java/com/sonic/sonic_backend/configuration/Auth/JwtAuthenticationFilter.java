package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import com.sonic.sonic_backend.domain.Member.repository.RefreshTokenRepository;
import com.sonic.sonic_backend.exception.LogOutToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("in auth filter");
            String token = resolveToken(request);
            if(token!=null) {
                //로그아웃 한 토큰인지 검증
                Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
                if(!refreshToken.isEmpty()) {
                    if(refreshToken.get().getMemberId().equals("blackList")) {
                        //TODO:: 필터내부 오류는 ControllerAdvice로 처리 불가
                        // -> catch해서 ServletResponse & ObjectMapper로 직접 예외처리
                        throw new LogOutToken();
                    }
                }
                //유효한 토큰인지 검증
                if (jwtProvider.validateToken(token)) {
                    Authentication authentication = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            System.out.println("done auth filter");
            filterChain.doFilter(request, response);
        } catch (LogOutToken e) {
            System.out.println(e);
        }
    }

    //헤더에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
