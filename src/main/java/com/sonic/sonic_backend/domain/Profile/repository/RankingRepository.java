package com.sonic.sonic_backend.domain.Profile.repository;

import com.sonic.sonic_backend.domain.Member.entity.Member;
import com.sonic.sonic_backend.domain.Profile.dto.RankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class RankingRepository {
    private final RedisTemplate redisTemplate;
    private final String KEY = "ranking";

    @Transactional
    public void addOrUpdate(String memberId, int exp) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(KEY, memberId, exp);
    }

    @Transactional
    public void increaseScore(String memberId, int exp) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.incrementScore(KEY, memberId, exp);
    }


    public int getMyRanking(Member member) {
        //1. 요청 회원의 순위 얻기
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.reverseRank(KEY, member.getId().toString()).intValue()+1;
    }

    public Long getScore(Member member) {
        ZSetOperations<String, String > zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.score(KEY, member.getId().toString()).longValue();
    }

    public double getAllCount() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.count(KEY, 0, Double.POSITIVE_INFINITY).doubleValue();
    }

    public List<RankingResponseDto> getRankingList(int myRanking) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        //2. 요청 회원의 -2, +2 순위 회원들의 id, score 얻기
        //ranking - range 차이 -> +-1 조정
        int min = myRanking-2; int max = myRanking+2;
        if(min<0) min=0; //음수 되지 않도록 조정, 최댓값 범위 넘어가는건 상관없음
        Set<ZSetOperations.TypedTuple<String>> rankingList = zSetOperations
                //내림차순 순위 : exp
                .reverseRangeWithScores(KEY,min,max);

        //
        List<RankingResponseDto> rankingResponseDto = new ArrayList<>();
        Iterator<ZSetOperations.TypedTuple<String>> iterator = rankingList.iterator();
        while(iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> IdScoreSet = iterator.next();
            rankingResponseDto.add(RankingResponseDto.builder()
                            .id(Long.valueOf(IdScoreSet.getValue()))
                            .exp(IdScoreSet.getScore().longValue())
                            .ranking(min+1)
                            .build());
            min++;
        }
        return rankingResponseDto;
    }
}
