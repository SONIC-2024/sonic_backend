package com.sonic.sonic_backend.domain.Member.repository;

import com.sonic.sonic_backend.domain.Member.entity.RefreshToken;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class AuthCodeRepository {
    private final RedisTemplate redisTemplate;
    public AuthCodeRepository(@Qualifier("redisTemplateForEmail") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    //10분 안에 인증
    private final int EXPIRE_TIME=60*10;

    // <key : code, value : email> 형태로 저장
    public void save(final int authCode, final String email) {
        System.out.println("start save");
        System.out.println("$%$% "+redisTemplate.getConnectionFactory());
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        System.out.println("done 1");
        valueOperations.set(email, authCode);
        System.out.println("done 2");
        redisTemplate.expire(email,EXPIRE_TIME, TimeUnit.SECONDS);
        System.out.println("all done");
    }

    public Optional<Integer> findById(String email) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        Integer authcode = valueOperations.get(email);
        if(Objects.isNull(authcode)) return Optional.empty();
        return Optional.of(authcode);
    }

}
