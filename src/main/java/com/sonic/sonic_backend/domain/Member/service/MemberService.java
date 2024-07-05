package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.dto.member.GetMemberNameResponseDto;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberSocialRepository;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
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


}
