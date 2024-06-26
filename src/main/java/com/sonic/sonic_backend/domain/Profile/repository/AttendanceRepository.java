package com.sonic.sonic_backend.domain.Profile.repository;

import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
