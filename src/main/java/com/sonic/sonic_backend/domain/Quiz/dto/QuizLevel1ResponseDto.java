package com.sonic.sonic_backend.domain.Quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class QuizLevel1ResponseDto {
    private String content;
    private ArrayList<Character> detailed_content;
    //String -> long으로 get만 해오므로 그냥 primitive type을 사용
    private long[] id;
    private boolean isStarred;

    public static QuizLevel1ResponseDto toDto(String content, ArrayList<Character> detailed_content, long[] id, boolean isStarred) {
        return QuizLevel1ResponseDto.builder()
                .content(content)
                .detailed_content(detailed_content)
                .id(id)
                .isStarred(isStarred)
                .build();
    }
}
