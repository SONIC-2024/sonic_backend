package com.sonic.sonic_backend.domain.Profile.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.dto.AttendanceResponseDto;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import com.sonic.sonic_backend.exception.SocialMemberUpdatePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberService memberService;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AttendanceResponseDto getAttendance() {
        Member member = memberService.getCurrentMember();
        WeekAttendance weekAttendance = member.getWeekAttendance();
        Attendance attendance = member.getAttendance();

        return new AttendanceResponseDto().toDto(getWeekList(weekAttendance), attendance.getContinuous_attendance());
    }

    public boolean[] getWeekList(WeekAttendance weekAttendance) {
        return new boolean[] {
        weekAttendance.isMON(), weekAttendance.isTUE(), weekAttendance.isWED()
                , weekAttendance.isTHU(), weekAttendance.isFRI(),weekAttendance.isSAT(), weekAttendance.isSUN() };
    }

    @Transactional
    public int updateExp(int exp) {
        MemberProfile memberProfile = getCurrentMemberProfile();
        memberProfile.addExp(exp);
        return memberProfile.getExp();
    }

    @Transactional
    public void updateProfileImg(MultipartFile file) throws IOException {
        MemberProfile memberProfile = getCurrentMemberProfile();
        String key = s3Service.saveProfileFile(file);

        String previousProfile = memberProfile.getProfileImgUrl();
        memberProfile.updateProfileImgUrl(key);
        if(!previousProfile.equals("profile.jpg")) s3Service.delete(key);
    }

    @Transactional
    public void updateHand(String hand) {
        getCurrentMemberProfile().updateHand(hand);
    }
    @Transactional
    public void updateNickname(String nickname) {
        getCurrentMemberProfile().updateNickname(nickname);
    }
    @Transactional
    public void updatePassword(String password) {
        MemberGeneral memberGeneral = memberGeneralRepository.findByMember(memberService.getCurrentMember());
        //TODO: 로직 변경시 삭제
        if(memberGeneral==null) {throw new SocialMemberUpdatePassword();}
        memberGeneral.updatePassword(passwordEncoder.encode(password));
    }

    private MemberProfile getCurrentMemberProfile() {
        Member member = memberService.getCurrentMember();
        return member.getMemberProfile();
    }

}
