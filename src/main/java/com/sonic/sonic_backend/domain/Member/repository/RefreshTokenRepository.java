package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    //테스트용, 추후에 1주로 설정
    private final int EXPIRE_TIME=60*2;

    // <key : token, value : memberId> 형태로 저장
    public void save(final RefreshToken refreshToken) {
        System.out.println("start save");
        System.out.println("$%$% "+redisTemplate.getConnectionFactory());
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        System.out.println("done 1");
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getMemberId());
        System.out.println("done 2");
        redisTemplate.expire(refreshToken.getRefreshToken(),EXPIRE_TIME, TimeUnit.SECONDS);
        System.out.println("all done");
    }

    public Optional<RefreshToken> findById(String refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        Long memberId = valueOperations.get(refreshToken);
        if(Objects.isNull(memberId)) return Optional.empty();
        return Optional.of(new RefreshToken(refreshToken, memberId));
    }

    public void delete(String refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(refreshToken);

    }

}
