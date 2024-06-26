package com.sonic.sonic_backend.domain.Profile.repository;

import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekAttendanceRepository extends JpaRepository<WeekAttendance, Long> {
}
