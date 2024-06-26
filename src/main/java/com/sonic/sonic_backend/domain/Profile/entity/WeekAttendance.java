package com.sonic.sonic_backend.domain.Profile.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekAttendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="week_attendance_id")
    private Long id;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean MON;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean TUE;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean WED;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean THU;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean FRI;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean SAT;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean SUN;
}
