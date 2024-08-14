package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {


    private final RedisTemplate redisTemplate;
    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private final int EXPIRE_TIME=7;
    private final int BLACK_LISt_EXPIRE_DAY = 7;

    // <key : token, value : memberId> 형태로 저장
    public void save(final RefreshToken refreshToken) {
        //reissue 시 find by token이 필요하므로 이중으로 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getMemberId());
        redisTemplate.expire(refreshToken.getRefreshToken(),EXPIRE_TIME, TimeUnit.DAYS);
        //로그아웃 시 find by id가 필요하므로 이중으로 저장
        valueOperations.set(refreshToken.getMemberId(), refreshToken.getRefreshToken());
        redisTemplate.expire(refreshToken.getMemberId(),EXPIRE_TIME, TimeUnit.DAYS);
    }

    public void saveBlackList(final String accessToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, "blackList");
        redisTemplate.expire(accessToken, BLACK_LISt_EXPIRE_DAY, TimeUnit.DAYS);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String memberId = valueOperations.get(refreshToken);
        if(Objects.isNull(memberId)) return Optional.empty();
        return Optional.of(new RefreshToken(refreshToken, memberId));
    }
    public Optional<RefreshToken> findById(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(id);
        if(Objects.isNull(refreshToken)) return Optional.empty();
        return Optional.of(new RefreshToken(refreshToken, id));
    }

    public void delete(String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        //key:token 삭제
        String id = valueOperations.getAndDelete(refreshToken);
        //key:id 삭제
        valueOperations.getAndDelete(id);
    }

}
