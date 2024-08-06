package com.sonic.sonic_backend.domain.Word.repository;

import com.sonic.sonic_backend.domain.Word.dto.WordListResponseDto;
import com.sonic.sonic_backend.domain.Word.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    Page<Word> findByCategory(String category, Pageable p);
}
