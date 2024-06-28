package com.sonic.sonic_backend.domain.Member.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="attendance_id")
    private Attendance attendance;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="member_profile_id")
    private MemberProfile memberProfile;
    @OneToOne(optional = false)
    @JoinColumn(name="week_attendance_id")
    private WeekAttendance weekAttendance;

}
