package com.sonic.sonic_backend.domain.Quiz.repository;

import com.sonic.sonic_backend.domain.Quiz.entity.SolvedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolvedQuizRepository extends JpaRepository<SolvedQuiz, Long> {
}
