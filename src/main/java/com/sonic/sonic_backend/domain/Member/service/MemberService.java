package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.dto.member.GetMemberNameResponseDto;
import com.sonic.sonic_backend.domain.Member.dto.member.MemberProfileResponseDto;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberSocialRepository;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Profile.repository.AttendanceRepository;
import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import com.sonic.sonic_backend.domain.Profile.repository.WeekAttendanceRepository;
import com.sonic.sonic_backend.exception.MemberNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGeneralRepository memberGeneralRepository;
    private final MemberSocialRepository memberSocialRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final WeekAttendanceRepository weekAttendanceRepository;
    private final AttendanceRepository attendanceRepository;
    private final S3Service s3Service;

    public Member getCurrentMember() {
        return memberRepository.findByEmail(
                (SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(MemberNotFound::new);
    }

    public GetMemberNameResponseDto getMemberName() {
        MemberProfile memberProfile = getCurrentMember().getMemberProfile();
        return GetMemberNameResponseDto.builder().name(memberProfile.getNickname()).build();
    }
    public MemberProfileResponseDto getMemberProfile() {
        MemberProfile memberProfile = getCurrentMember().getMemberProfile();
        return MemberProfileResponseDto.builder()
                .nickname(memberProfile.getNickname())
                .profileImg(s3Service.getFullUrl(memberProfile.getProfileImgUrl()))
                .build();
    }

    @Transactional
    public void deleteMember() {
        Member member = getCurrentMember();

        attendanceRepository.delete(member.getAttendance());
        weekAttendanceRepository.delete(member.getWeekAttendance());
        memberProfileRepository.delete(member.getMemberProfile());

        String email = member.getEmail();
        memberRepository.delete(member);

        if(email.contains("@")) {
            memberGeneralRepository.delete(memberGeneralRepository.findByMember(member));
        } else {
            memberSocialRepository.delete(memberSocialRepository.findByMember(member));
        }
    }

    @Transactional
    public void updateAttendance() {
        Member member = getCurrentMember();
        Attendance attendance = member.getAttendance(); WeekAttendance weekAttendance = member.getWeekAttendance();
        LocalDate now = LocalDate.now();

        updateContinuousAttendance(attendance, now);
        updateWeekAttendance(weekAttendance, now);
        attendance.updateLastDate(now);
    }

    private void updateWeekAttendance(WeekAttendance weekAttendance, LocalDate now) {
        switch (now.getDayOfWeek().getValue()) {
            case 1 -> weekAttendance.updateOnMon();
            case 2 -> weekAttendance.updateOnTue();
            case 3 -> weekAttendance.updateOnWed();
            case 4 -> weekAttendance.updateOnThu();
            case 5 -> weekAttendance.updateOnFri();
            case 6 -> weekAttendance.updateOnSat();
            case 7 -> weekAttendance.updateOnSun();
        }
    }

    private void updateContinuousAttendance(Attendance attendance, LocalDate now) {
        LocalDate lastAttendance = attendance.getLast_date();
        //연속 출석일수 갱신
        if(lastAttendance.plusDays(1).isEqual(now)) {
            attendance.plusContinuousAttendance();
            //최대 연속 출석일수 갱신
            if(attendance.getMax_continuous_attendance() < attendance.getContinuous_attendance()) {
                attendance.plusMaxContinuousAttendance();
            }
        } else if(!lastAttendance.isEqual(LocalDate.now())) {
            attendance.initContinuousAttendance();
        }
    }


}
