package com.sonic.sonic_backend.domain.Member.dto.common;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.entity.MemberSocial;
import com.sonic.sonic_backend.domain.Member.entity.Role;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Tier;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SignUpRequestDto {
    private String email;
    @Size(min = 4, max = 15, message = "최소 4자 이상, 15자 이하의 숫자를 입력하세요")
    @Pattern(regexp = "^[a-zA-Z0-9|@|!]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자(@, !)만 입력 가능합니다.")
    private String password;
    @Length(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하로 입력해주세요")
    private String nickname;
    private String hand;
    private int emailCode;

    public  Member toMemberEntity(String email, MemberProfile memberProfile, Attendance attendance, WeekAttendance weekAttendance) {
        return Member.builder()
                .email(email)
                .memberProfile(memberProfile)
                .attendance(attendance)
                .weekAttendance(weekAttendance)
                .role(Role.ROLE_USER)
                .build();
    }
    public  MemberGeneral toMemberGeneralEntity(Member member, String password) {
        return MemberGeneral.builder()
                .member(member)
                .password(password)
                .build();
    }
    public MemberSocial toMemberSocialEntity(Member member, Long socialId) {
        return MemberSocial.builder()
                .socialId(socialId)
                .member(member)
                .provider("KAKAO")
                .build();
    }
    public MemberProfile toMemberProfileEntity(SignUpRequestDto signUpRequestDto,String basicProfileUrl) {
        return MemberProfile.builder()
                .tier(Tier.BRONZE_III)
                .nickname(signUpRequestDto.getNickname())
                .profileImgUrl(basicProfileUrl)
                .exp(0)
                .hand(signUpRequestDto.getHand())
                .build();
    }

}
