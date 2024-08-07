package com.sonic.sonic_backend.domain.Quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Getter
public class SolvedQuizNumberResponseDto {
    private Long level1;
    private Long level2;
    private Long level3;

    public SolvedQuizNumberResponseDto(Long level1Count, Long level2Count, Long level3Count) {
        this.level1 = level1Count;
        this.level2 = level2Count;
        this.level3 = level3Count;
    }
}
