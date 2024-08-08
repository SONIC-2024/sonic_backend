package com.sonic.sonic_backend.domain.Profile.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RankingResponseDto {
    /*
    "ranking": 3,
            "tierImg": "https://kimgreen.s3.us-east-2.amazonaws.com/03ffb02c-5d94-4789-a4a5-4cc90df77b03.jpg",
            "nickname": "김지은",
            "profileImg": "https://kimgreen.s3.us-east-2.amazonaws.com/03ffb02c-5d94-4789-a4a5-4cc90df77b03.jpg",
            "exp": 12,
            "attendance": 3
     */
    private Long ranking;
    private String tierImg;
    private String profileImg;
    private Long exp;
    private int attendance;
    private Long id;
    private String nickname;

    public void updateSameRanking(Long ranking) {this.ranking = ranking;}
    public void updateDto(String tierImg, String profileImg, int attendance, String nickname) {
        this.tierImg = tierImg;
        this.profileImg = profileImg;
        this.attendance = attendance;
        this.nickname = nickname;
    }
}
