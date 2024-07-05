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
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String hand;
    private String passwordCheck;
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
