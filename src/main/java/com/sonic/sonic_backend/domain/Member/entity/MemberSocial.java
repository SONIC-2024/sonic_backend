package com.sonic.sonic_backend.domain.Member.entity;

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
public class MemberSocial extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_social_id")
    private Long id;

    @ColumnDefault("'KAKAO'")
    private String provider;

    @Column(name="social_id")
    private Long socialId;

    @OneToOne(optional = false)
    @JoinColumn(name="member_id")
    private Member member;
}
