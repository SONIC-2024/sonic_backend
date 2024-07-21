package com.sonic.sonic_backend.domain.Profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AttendanceResponseDto {
    private boolean MON, TUE, WED, THU, FRI, SAT, SUN;
    private int continuous;

    public static AttendanceResponseDto toDto(boolean[] week, int continuous) {
        return AttendanceResponseDto.builder()
                .MON(week[0]).TUE(week[1]).WED(week[2]).THU(week[3]).FRI(week[4]).SAT(week[5]).SUN(week[6])
                .continuous(continuous)
                .build();
    }
}
