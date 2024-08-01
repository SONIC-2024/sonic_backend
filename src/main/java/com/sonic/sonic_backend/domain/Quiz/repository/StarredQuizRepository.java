package com.sonic.sonic_backend.domain.Quiz.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Quiz.entity.StarredQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarredQuizRepository extends JpaRepository<StarredQuiz, Long> {
    Optional<StarredQuiz> findByMemberAndQuizId(Member member, Long quizId);
    boolean existsByMemberAndQuizId(Member member, Long quizId);
}
