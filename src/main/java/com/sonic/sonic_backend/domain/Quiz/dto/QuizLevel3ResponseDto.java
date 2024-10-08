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
    private Long quiz_id;
    private String content;
    private boolean isStarred;

    public static QuizLevel3ResponseDto toDto(Long id, String content, boolean isStarred,Long quiz_id) {
        return QuizLevel3ResponseDto.builder()
                .id(id)
                .content(content)
                .quiz_id(quiz_id)
                .isStarred(isStarred)
                .build();
    }
}
