package com.alibaba.example.aidemo.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisMemoryConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository(){
        return RedisChatMemoryRepository.builder()
                .host(host)
                .port(port)
                .build();
    }

}
