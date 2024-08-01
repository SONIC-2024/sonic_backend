package com.sonic.sonic_backend.domain.Quiz.entity;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SolvedQuiz {

    @EmbeddedId
    @Column(name="solved_quiz_id")
    private SolvedQuizId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @MapsId("quizId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Builder
    public SolvedQuiz(SolvedQuizId solvedQuizId, Member member, Quiz quiz) {
        this.id = new SolvedQuizId(member.getId(), quiz.getId());
        this.member = member;
        this.quiz = quiz;
    }
}
