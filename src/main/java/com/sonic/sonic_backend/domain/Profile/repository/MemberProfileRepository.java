package com.sonic.sonic_backend.domain.Profile.repository;

import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
