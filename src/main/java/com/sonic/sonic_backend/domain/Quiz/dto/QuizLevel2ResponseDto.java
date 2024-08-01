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
    private String objectUrl;

    public static QuizLevel2ResponseDto toDto(String answer, String[] content, String objectUrl) {
        return QuizLevel2ResponseDto.builder()
                .answer(answer)
                .content(content)
                .objectUrl(objectUrl)
                .build();
    }
}
