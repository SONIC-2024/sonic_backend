package com.sonic.sonic_backend.domain.Profile.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import com.sonic.sonic_backend.domain.Tier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_profile_id")
    private Long id;

    private String nickname;

    private String hand;

    private int exp;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Column(name="profile_img_url")
    private String profileImgUrl;

    public void addExp(int exp) {this.exp+=exp;}
}
