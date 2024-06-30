package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.domain.Member.dto.member.GetMemberNameResponseDto;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.exception.MemberNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getCurrentMember() {
        return memberRepository.findByEmail(
                (SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(MemberNotFound::new);
    }

    public GetMemberNameResponseDto getMemberName() {
        MemberProfile memberProfile = getCurrentMember().getMemberProfile();
        return GetMemberNameResponseDto.builder().name(memberProfile.getNickname()).build();
    }
}
