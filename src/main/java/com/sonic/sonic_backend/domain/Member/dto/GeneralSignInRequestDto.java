package com.sonic.sonic_backend.domain.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GeneralSignInRequestDto {
    private String email;
    private String password;
}
