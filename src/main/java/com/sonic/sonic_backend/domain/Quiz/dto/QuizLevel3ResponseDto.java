package com.sonic.sonic_backend.domain.Quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class QuizLevel3ResponseDto {
    private Long id;
    private String content;

    public static QuizLevel3ResponseDto toDto(Long id, String content) {
        return QuizLevel3ResponseDto.builder()
                .id(id)
                .content(content)
                .build();
    }
}
