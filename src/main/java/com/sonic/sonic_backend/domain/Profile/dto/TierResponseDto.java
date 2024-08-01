package com.sonic.sonic_backend.domain.Profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TierResponseDto {
    private String tierImg;
    private String tier;
    private int top;
    private Long exp;

    public static TierResponseDto toDto(String tierImg, String tier, int top, Long exp) {
        return TierResponseDto.builder()
                .tierImg(tierImg)
                .tier(tier)
                .exp(exp)
                .top(top)
                .build();
    }
}
