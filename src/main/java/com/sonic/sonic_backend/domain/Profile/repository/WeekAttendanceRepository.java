package com.sonic.sonic_backend.domain.Profile.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WeekAttendanceRepository extends JpaRepository<WeekAttendance, Long> {
}
