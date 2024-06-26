package com.sonic.sonic_backend.domain.Profile.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attendance_id")
    private Long id;

    @Column(name="last_date")
    @Temporal(TemporalType.DATE)
    private LocalDate last_date;

    @Column(name="continuous_attendance")
    @ColumnDefault("0")
    private int continuous_attendance;

    @Column(name="max_continuous_attendance")
    @ColumnDefault("0")
    private int max_continuous_attendance;
}
