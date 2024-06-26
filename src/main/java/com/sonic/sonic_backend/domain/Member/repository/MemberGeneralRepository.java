package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.MemberGeneral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGeneralRepository extends JpaRepository<MemberGeneral, Long> {
}
