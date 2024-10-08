package com.sonic.sonic_backend.domain.Profile.service;

import com.sonic.sonic_backend.configuration.AWS.S3Service;
import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Member.repository.MemberRepository;
import com.sonic.sonic_backend.domain.Member.service.MemberService;
import com.sonic.sonic_backend.domain.Profile.dto.RankingResponseDto;
import com.sonic.sonic_backend.domain.Profile.repository.RankingRepository;
import com.sonic.sonic_backend.exception.MemberNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;
    private final MemberService memberService;
    private final ProfileService profileService;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    //@Transactional
    public List<RankingResponseDto> getRanking() {
        Member member = memberService.getCurrentMember();
        //1. 요청한 회원의 순위 구하기
        Long myRanking = rankingRepository.getRankingById(member.getId());
        //2. 요청 회원의 -2, +2 순위 회원들의 id, score, ranking 얻기
        List<RankingResponseDto> rankingResponseDtos = rankingRepository.getRankingList(myRanking);
        //3. 동점자 순위 처리
        getCompleteRanking(rankingResponseDtos);

        //4. dto의 나머지 부분들 채우기 : attendance, profile, tier img url
        return getCompleteDto(rankingResponseDtos);
    }
    public void getCompleteRanking(List<RankingResponseDto> rankingResponseDtos) {
        Long exScore = rankingResponseDtos.get(0).getExp();
        Long score;
        for (int i = 0; i < rankingResponseDtos.size(); i++) {
            score = rankingResponseDtos.get(i).getExp();
            if(i!=0) {
                if(exScore==score) {
                    rankingResponseDtos.get(i)
                            .updateSameRanking(rankingResponseDtos.get(i-1).getRanking());
                }
            }
            exScore = score;
        }
    }

    private List<RankingResponseDto> getCompleteDto(List<RankingResponseDto> rankingResponseDtos) {
        for(RankingResponseDto dto : rankingResponseDtos) {
            Member foundMember = memberRepository.findById(dto.getId()).orElseThrow(MemberNotFound::new);
            profileService.updateRankingTiers(foundMember);
            dto.updateDto(
                    s3Service.getFullUrl(foundMember.getMemberProfile().getTier().url),
                    s3Service.getFullUrl(foundMember.getMemberProfile().getProfileImgUrl()),
                    foundMember.getAttendance().getContinuous_attendance(),
                    foundMember.getMemberProfile().getNickname());
        }
        return rankingResponseDtos;
    }
}
