package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import com.sonic.sonic_backend.domain.Member.repository.RefreshTokenRepository;
import com.sonic.sonic_backend.exception.LogOutToken;
import com.sonic.sonic_backend.exception.NoAuthorizationHeader;
import com.sonic.sonic_backend.exception.NoBearer;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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
import java.security.SignatureException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final static String EXCEPTION = "exception";
    private final static String LOGOUT = "logout";
    private final static String MALFORMED = "malformed";
    private final static String EXPIRED = "expired";
    private final static String HEADER = "header";
    private final static String BEARER = "bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("in auth filter");
            String token = resolveToken(request);
            //로그아웃 한 토큰인지 검증
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
            if(!refreshToken.isEmpty()) {
                if(refreshToken.get().getMemberId().equals("blackList")) {
                    //DONE:: 필터내부 오류는 ControllerAdvice로 처리 불가
                    // -> catch해서 AuthenticationEntryPoint : ServletResponse & ObjectMapper로 직접 예외처리
                    throw new LogOutToken();
                }
            }
            //유효한 토큰인지 검증
            if (jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (LogOutToken e) {
            request.setAttribute(EXCEPTION,LOGOUT);
        } catch (MalformedJwtException e) {
            request.setAttribute(EXCEPTION,MALFORMED);
        } catch (ExpiredJwtException e) {
            request.setAttribute(EXCEPTION, EXPIRED);
        } catch (NoAuthorizationHeader e) {
            request.setAttribute(EXCEPTION, HEADER);
        } catch (NoBearer e) {
            request.setAttribute(EXCEPTION,BEARER);
        }
        //위치 변경
        System.out.println("done auth filter");
        filterChain.doFilter(request, response);

    }

    //헤더에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(!StringUtils.hasText(bearerToken)) throw new NoAuthorizationHeader();
        if(!bearerToken.startsWith("Bearer")) throw new NoBearer();
        return bearerToken.substring(7);
    }
}
