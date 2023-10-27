package com.tickerBell.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Profile("test")
@Configuration
@Testcontainers
public class MockRedisConfig {

    @Container
    public static GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactoryMock() {
        redis.start();
        return new LettuceConnectionFactory(redis.getHost(), redis.getFirstMappedPort());
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> redisTemplateMock() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // setKeySerializer, setValueSerializer 설정
        // redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactoryMock());

        return redisTemplate;
    }
}
