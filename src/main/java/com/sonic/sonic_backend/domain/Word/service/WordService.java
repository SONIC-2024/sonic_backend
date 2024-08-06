package com.sonic.sonic_backend.domain.Word.service;

import com.sonic.sonic_backend.domain.Word.dto.WordListResponseDto;
import com.sonic.sonic_backend.domain.Word.repository.WordRepository;
import com.sonic.sonic_backend.exception.TypeNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final String CONSONANT = "consonant";
    private final String VOWEL = "vowel";
    private final String WORD = "word";

    @Transactional(readOnly = true)
    public Page<WordListResponseDto> getList(String c, Pageable p) {
        return wordRepository.findByCategory(chooseCategory(c), p)
                .map(WordListResponseDto::toDto);
    }
    public String chooseCategory(String c) {
        switch (c) {
            case "c" -> { return CONSONANT; }
            case "v" -> { return VOWEL; }
            case "w" -> { return WORD; }
            default ->  throw new TypeNotFound();
        }
    }


}
