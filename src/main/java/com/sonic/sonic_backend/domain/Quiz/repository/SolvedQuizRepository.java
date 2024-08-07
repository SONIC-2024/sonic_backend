package com.sonic.sonic_backend.domain.Quiz.repository;

import com.sonic.sonic_backend.domain.Quiz.dto.SolvedQuizNumberResponseDto;
import com.sonic.sonic_backend.domain.Quiz.entity.SolvedQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolvedQuizRepository extends JpaRepository<SolvedQuiz, Long> {
    @Query("SELECT new com.sonic.sonic_backend.domain.Quiz.dto.SolvedQuizNumberResponseDto"+
            "(SUM(CASE WHEN q.level = 1 THEN 1 ELSE 0 END),"+
            "SUM(CASE WHEN q.level = 2 THEN 1 ELSE 0 END),"+
            "SUM(CASE WHEN q.level = 3 THEN 1 ELSE 0 END)) "+
            "FROM SolvedQuiz s "+
            "JOIN s.quiz q " +
            "WHERE s.id.memberId = :id")
    SolvedQuizNumberResponseDto getSolvedQuizNumbers(@Param("id") Long id);
}
