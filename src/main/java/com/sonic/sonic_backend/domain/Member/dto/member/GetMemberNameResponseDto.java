package com.sonic.sonic_backend.domain.Member.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.beans.ConstructorProperties;

@Jacksonized
@Getter
@Builder
public class GetMemberNameResponseDto {
    private String name;
}
