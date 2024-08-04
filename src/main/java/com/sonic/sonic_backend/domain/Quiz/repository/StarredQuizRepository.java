package com.sonic.sonic_backend.domain.Quiz.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Quiz.dto.StarredQuiz1Or3ResponseDto;
import com.sonic.sonic_backend.domain.Quiz.dto.StarredQuiz2ResponseDto;
import com.sonic.sonic_backend.domain.Quiz.entity.StarredQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StarredQuizRepository extends JpaRepository<StarredQuiz, Long> {
    Optional<StarredQuiz> findByMemberAndQuizId(Member member, Long quizId);
    boolean existsByMemberAndQuizId(Member member, Long quizId);

    @Query("select new com.sonic.sonic_backend.domain.Quiz.dto.StarredQuiz1Or3ResponseDto(q.id, q.content) " +
            "from StarredQuiz s " +
            "join s.quiz q " +
            "where s.member.id=:id and q.level=1")
    Page<StarredQuiz1Or3ResponseDto> findLevel1(@Param("id") Long id, Pageable pageable);

    @Query("select new com.sonic.sonic_backend.domain.Quiz.dto.StarredQuiz1Or3ResponseDto(q.id, q.content) " +
            "from StarredQuiz s " +
            "join s.quiz q " +
            "where s.member.id=:id and q.level=3")
    Page<StarredQuiz1Or3ResponseDto> findLevel3(@Param("id") Long id, Pageable pageable);

    @Query("select new com.sonic.sonic_backend.domain.Quiz.dto.StarredQuiz2ResponseDto(q.id) " +
            "from StarredQuiz s " +
            "join s.quiz q " +
            "where s.member.id=:id and q.level=2")
    Page<StarredQuiz2ResponseDto> findLevel2(@Param("id") Long id, Pageable pageable);
}
