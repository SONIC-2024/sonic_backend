package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.entity.MemberSocial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialRepository extends JpaRepository<MemberSocial, Long> {
    MemberSocial findByMember(Member member);
}
