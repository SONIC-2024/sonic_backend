package com.sonic.sonic_backend.domain.Quiz.repository;

import com.sonic.sonic_backend.domain.Quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
