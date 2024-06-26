package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.domain.Member.dto.SignUpRequestDto;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.repository.MemberGeneralRepository;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Profile.repository.AttendanceRepository;
import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import com.sonic.sonic_backend.domain.Profile.repository.WeekAttendanceRepository;
import com.sonic.sonic_backend.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendanceRepository attendanceRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final WeekAttendanceRepository weekAttendanceRepository;
    private final MemberGeneralRepository memberGeneralRepository;

    public void signUp(SignUpRequestDto signUpRequestDto) {
        String email = signUpRequestDto.getEmail();
        //중복이메일검증
        checkIfDuplicated(email);
        //유효이메일검증
        checkValidEmail(signUpRequestDto.getEmailCode());
        saveMember(signUpRequestDto);
    }

    @Transactional
    private void saveMember(SignUpRequestDto signUpRequestDto) {
        MemberProfile memberProfile = memberProfileRepository.save(signUpRequestDto.toMemberProfileEntity(signUpRequestDto));
        Attendance attendance = attendanceRepository.save(getEmptyAttendance());
        WeekAttendance weekAttendance = weekAttendanceRepository.save(getEmptyWeekAttendance());
        Member member = signUpRequestDto.toMemberEntity(signUpRequestDto.getEmail(),memberProfile,attendance,weekAttendance);
        memberRepository.save(member);
        /*
            일단 일반로그인의 회원가입만 구현,카카오로그인 구현과정에 카카오 회원가입로직 추가
         */
        memberGeneralRepository.save(signUpRequestDto.toMemberGeneralEntity(member,passwordEncoder.encode(signUpRequestDto.getPassword())));
    }


    private void checkIfDuplicated(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmail();
        }
    }
    private void checkValidEmail(String email) {

    }

    private WeekAttendance getEmptyWeekAttendance() {
        return WeekAttendance.builder()
                .MON(false).TUE(false).WED(false).THU(false).FRI(false).SAT(false).SUN(false).build();
    }

    private Attendance getEmptyAttendance() {
        return Attendance.builder()
                .last_date(null)
                .continuous_attendance(0).max_continuous_attendance(0)
                .build();
    }
}
