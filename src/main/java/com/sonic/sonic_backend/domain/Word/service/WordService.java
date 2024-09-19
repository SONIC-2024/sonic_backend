package com.sonic.sonic_backend.domain.Word.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
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
    private final MemberService memberService;
    private final S3Service s3Service;
    private final String CONSONANT = "consonant";
    private final String VOWEL = "vowel";
    private final String WORD = "word";
    private final long WORD_LEFT_HAND_WEIGHT=1L;
    private final long VOWEL_CONSONANT_LEFT_HAND_WEIGHT=35L;
    private final String RIGHT="right";

    @Transactional(readOnly = true)
    public Page<WordListResponseDto> getList(String c, Pageable p) {
        return wordRepository.findByCategoryAndHand(chooseCategory(c), RIGHT ,p)
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
        String hand = memberService.getCurrentMember().getMemberProfile().getHand();
        Long id = word.getId();
        if(word.getCategory().equals(WORD)) {
            return WordResponseDto
                    .toDto(getWordIds(hand, id), word.getContent(), s3Service.getFullUrl(word.getObjectUrl()));
        } else {
            return WordResponseDto
                    .toDto(getVowelOrConsonantIds(hand, id), word.getContent(), s3Service.getFullUrl(word.getObjectUrl()));
        }

    }

    public ArrayList<Long> getWordIds(String hand, Long id) {
        ArrayList<Long> ids = new ArrayList<>();

        long handWeight=0L;
        if(hand.equals("left")) handWeight=WORD_LEFT_HAND_WEIGHT;
        ids.add(id+handWeight);

        return ids;
    }
    public ArrayList<Long> getVowelOrConsonantIds(String hand, Long id) {
        ArrayList<Long> ids = new ArrayList<>();

        long handWeight=0L;
        if(hand.equals("left")) handWeight=VOWEL_CONSONANT_LEFT_HAND_WEIGHT;
        id+=handWeight;

        switch (id.intValue()) {
            case 32 -> {ids.add(19L); ids.add(15L);}
            case 33 -> {ids.add(19L); ids.add(25L);}
            case 34 -> {ids.add(21L); ids.add(17L);}
            case 35 -> {ids.add(21L); ids.add(27L);}
            case 67 -> {ids.add(54L); ids.add(50L);}
            case 68 -> {ids.add(54L); ids.add(60L);}
            case 69 -> {ids.add(56L); ids.add(52L);}
            case 70 -> {ids.add(56L); ids.add(62L);}
            default -> ids.add(id);
        }
        return ids;
    }


}
