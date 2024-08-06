package com.sonic.sonic_backend.domain.Word.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WordResponseDto {
    private Long id;
    private String content;
    private String object_url;

    public static WordResponseDto toDto(Long id, String content, String object_url) {
        return WordResponseDto.builder()
                .id(id)
                .content(content)
                .object_url(object_url)
                .build();
    }
}
