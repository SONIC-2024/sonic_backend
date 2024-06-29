package com.sonic.sonic_backend.domain.Member.dto.general;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

//field 1개일때 역직렬화 문제 해결
@Jacksonized
@Getter
@Builder
public class MailSendRequestDto {
    private String email;
}
