package com.sonic.sonic_backend.domain.Profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequestDto {
    private String nickname;
    private String password;
    private String hand;
    private int exp;

}
