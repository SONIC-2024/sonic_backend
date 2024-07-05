package com.sonic.sonic_backend.domain.Profile.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequestDto {
    @Length(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하로 입력해주세요")
    private String nickname;
    @Size(min = 4, max = 15, message = "최소 4자 이상, 15자 이하의 숫자를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9|@|!]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자(@, !)만 입력 가능합니다.")
    private String password;
    private String hand;
    private int exp;

}
