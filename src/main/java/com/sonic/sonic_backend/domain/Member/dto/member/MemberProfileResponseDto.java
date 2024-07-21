package com.sonic.sonic_backend.domain.Member.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileResponseDto {
    private String nickname;
    private String profileImg;


}
