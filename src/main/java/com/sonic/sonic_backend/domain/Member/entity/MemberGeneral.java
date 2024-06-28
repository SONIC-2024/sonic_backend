package com.sonic.sonic_backend.domain.Member.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
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
public class MemberGeneral extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_general_id")
    private Long id;

    @Column(nullable = false)
    private String password;

    @OneToOne(optional = false)
    @JoinColumn(name="member_id")
    private Member member;

    public void changePassword(String password) {this.password = password;}
}
