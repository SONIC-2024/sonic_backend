package com.sonic.sonic_backend.domain.Member.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueDto {
    private String accessToken;
    private String refreshToken;
}
