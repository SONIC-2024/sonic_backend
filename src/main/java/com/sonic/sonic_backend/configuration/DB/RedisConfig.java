package com.sonic.sonic_backend.configuration.DB;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    //Lettuce 사용
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        System.out.println("redis: "+host+", "+port);
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        System.out.println("$%$%"+redisTemplate);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        System.out.println("done settin connection");
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));  // value
        System.out.println(redisTemplate.getConnectionFactory());
        return redisTemplate;
    }
    @Bean
    @Qualifier("redisTemplateForEmail")
    public RedisTemplate<?, ?> redisTemplateForEmail() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        System.out.println("$%$%"+redisTemplate);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        System.out.println("done settin connection");
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));  // value
        System.out.println(redisTemplate.getConnectionFactory());
        return redisTemplate;
    }

}
