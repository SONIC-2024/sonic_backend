package com.sonic.sonic_backend.domain.Word.entity;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "starred_word")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarredWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="starred_word_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;
}
