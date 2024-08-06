package com.sonic.sonic_backend.domain.Word.dto;

import com.sonic.sonic_backend.domain.Word.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WordListResponseDto {
    private Long id;
    private String title;

    public static WordListResponseDto toDto(Word word) {
        return WordListResponseDto.builder()
                .title(word.getContent())
                .id(word.getId())
                .build();
    }
}
