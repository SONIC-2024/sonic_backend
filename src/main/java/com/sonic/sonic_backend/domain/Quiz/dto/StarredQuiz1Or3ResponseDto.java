package com.sonic.sonic_backend.domain.Quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Builder
public class StarredQuiz1Or3ResponseDto {
    private Long id;
    private String title;


    public StarredQuiz1Or3ResponseDto(Long id, String title) {
        this.id = id; this.title = title;
    }
    public StarredQuiz1Or3ResponseDto(Long id) {
        this.id = id;
    }
}
