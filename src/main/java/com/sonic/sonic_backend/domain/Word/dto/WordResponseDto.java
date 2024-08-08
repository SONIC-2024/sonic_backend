package com.sonic.sonic_backend.domain.Word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WordResponseDto {
    private ArrayList<Long> id;
    private String content;
    private String object_url;

    public static WordResponseDto toDto(ArrayList<Long> id, String content, String object_url) {
        return WordResponseDto.builder()
                .id(id)
                .content(content)
                .object_url(object_url)
                .build();
    }
}
