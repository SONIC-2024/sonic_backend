package com.sonic.sonic_backend.configuration.Auth;

import com.sonic.sonic_backend.domain.Member.dto.common.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    //access token 유효기간
    private final long EXPIRE_TIME_ACCESS = 1000 * 60 * 30;
    private final long EXPIRE_TIME_REFRESH = 1000 * 60 * 60 * 24 * 7;
    private final Key key;
    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Authentication authentication) {
        //사용자 권한정보
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        //access token expire time
        long now = (new Date()).getTime();
        Date AccessTokenExpireTime = new Date(now+EXPIRE_TIME_ACCESS);
        //access token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth",authorities)
                .setExpiration(AccessTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        //refresh token 생성
        Date refreshTokenExpireTime = new Date(now + EXPIRE_TIME_REFRESH);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
        /*
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("Invalid Token "+e);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired Token "+e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported Token "+e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty."+e);
        }
        return false;
         */
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        //claim -> email(name)과 권한정보 받아옴 -> UserDetails 생성
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);
        //Authentication의 구현체 얻음
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims parseClaims(String accessToken) {
        //reissue 하는 경우에 토큰 관련 exception은 여기서 걸림
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("잘못된 형식의 토큰입니다");
        }
    }
}
