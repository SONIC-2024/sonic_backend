package com.sonic.sonic_backend.domain.Member.dto.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {

    //회원 번호
    @JsonProperty("id")
    public Long id;

    @JsonProperty("connected_at")
    public Date connectedAt;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;


    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        //프로필 : 닉네임
        @JsonProperty("profile")
        public Profile profile;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class Profile {
            //닉네임
            @JsonProperty("nickname")
            public String nickName;
        }

    }

}

