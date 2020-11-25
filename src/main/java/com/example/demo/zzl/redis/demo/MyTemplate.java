package com.example.demo.zzl.redis.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author HP
 * @Description TODO
 * @date 2020/11/25-22:11
 */
@Configuration
public class MyTemplate {


    @Bean
    public StringRedisTemplate ooxx(RedisConnectionFactory fc) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(fc);

        stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return stringRedisTemplate;
    }
}
