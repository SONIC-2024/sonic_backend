package com.sonic.sonic_backend.domain.Quiz.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SolvedQuizId implements Serializable {
    private Long memberId;
    private Long quizId;
}
