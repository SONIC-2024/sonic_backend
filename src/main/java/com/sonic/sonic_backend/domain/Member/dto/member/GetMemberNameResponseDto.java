package com.sonic.sonic_backend.domain.Member.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.beans.ConstructorProperties;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GetMemberNameResponseDto {
    private String name;
    private boolean isAuthenticated;
}
