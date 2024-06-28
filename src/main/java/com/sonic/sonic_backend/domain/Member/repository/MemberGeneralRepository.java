package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, Long> {
    Optional<MemberGeneral> findByMember(Member member);
}
