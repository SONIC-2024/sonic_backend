package com.sonic.sonic_backend.domain.Word.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Word.dto.WordListResponseDto;
import com.sonic.sonic_backend.domain.Word.dto.WordResponseDto;
import com.sonic.sonic_backend.domain.Word.entity.Word;
import com.sonic.sonic_backend.domain.Word.repository.WordRepository;
import com.sonic.sonic_backend.exception.TypeNotFound;
import com.sonic.sonic_backend.exception.WordNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final S3Service s3Service;
    private final String CONSONANT = "consonant";
    private final String VOWEL = "vowel";
    private final String WORD = "word";

    @Transactional(readOnly = true)
    public Page<WordListResponseDto> getList(String c, Pageable p) {
        return wordRepository.findByCategory(chooseCategory(c), p)
                .map(WordListResponseDto::toDto);
    }
    @Transactional(readOnly = true)
    public WordResponseDto getWord(Long id) {
        return getDto(wordRepository.findById(id).orElseThrow(WordNotFound::new));
    }

    public String chooseCategory(String c) {
        switch (c) {
            case "c" -> { return CONSONANT; }
            case "v" -> { return VOWEL; }
            case "w" -> { return WORD; }
            default ->  throw new TypeNotFound();
        }
    }
    public WordResponseDto getDto(Word word) {
        ArrayList<Long> ids = new ArrayList<>();
        Long id = word.getId();
        switch (id.intValue()) {
            case 32 -> {ids.add(19L); ids.add(15L);}
            case 33 -> {ids.add(19L); ids.add(25L);}
            case 34 -> {ids.add(21L); ids.add(17L);}
            case 35 -> {ids.add(21L); ids.add(27L);}
            default -> ids.add(id);
        }
        return WordResponseDto
                .toDto(ids, word.getContent(), s3Service.getFullUrl(word.getObjectUrl()));
    }


}
