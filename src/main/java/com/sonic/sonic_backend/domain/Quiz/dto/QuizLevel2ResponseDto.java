package com.sonic.sonic_backend.domain.Quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class QuizLevel2ResponseDto {
    private String answer;
    private String[] content;
    private Long quiz_id;
    private String objectUrl;
    private boolean isStarred;

    public static QuizLevel2ResponseDto toDto(String answer, String[] content, String objectUrl, boolean isStarred,Long quiz_id) {
        return QuizLevel2ResponseDto.builder()
                .answer(answer)
                .content(content)
                .quiz_id(quiz_id)
                .objectUrl(objectUrl)
                .isStarred(isStarred)
                .build();
    }
}
