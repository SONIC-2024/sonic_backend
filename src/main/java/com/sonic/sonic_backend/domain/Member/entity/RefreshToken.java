package com.sonic.sonic_backend.domain.Member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    //Redis의 key : "필드이름(refreshToken):값(토큰값)" 향테
    private String refreshToken;
    private Long memberId;
}
