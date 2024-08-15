package com.sonic.sonic_backend.domain.Member.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import com.sonic.sonic_backend.domain.Profile.entity.Attendance;
import com.sonic.sonic_backend.domain.Profile.entity.MemberProfile;
import com.sonic.sonic_backend.domain.Profile.entity.WeekAttendance;
import com.sonic.sonic_backend.domain.Quiz.entity.SolvedQuiz;
import com.sonic.sonic_backend.domain.Quiz.entity.StarredQuiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    //social 회원은 socialId를 저장
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
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="week_attendance_id")
    private WeekAttendance weekAttendance;


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SolvedQuiz> solvedQuiz = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StarredQuiz> starredQuiz = new ArrayList<>();

}
