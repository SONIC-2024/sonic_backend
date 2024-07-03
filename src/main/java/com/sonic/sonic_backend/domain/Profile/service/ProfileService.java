package com.sonic.sonic_backend.domain.Profile.service;

import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
}
