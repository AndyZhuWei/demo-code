package com.example.demo.zzl.redis.demo;

import com.example.demo.DemoCodeApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author HP
 * @Description 通过Spring-data-redis来使用Redis
 * 在application.yml中对redis进行配置
 * @date 2020/11/24-9:56
 */
@SpringBootApplication
public class SpringDataRedis {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringDataRedis.class, args);
        TestRedis redis = context.getBean(TestRedis.class);

        redis.testRedis();

    }


}
