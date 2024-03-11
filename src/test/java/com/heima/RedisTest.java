package com.heima;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest // 如果在测试类上添加了该注解 那么在测试执行前 会先初始化spring容器
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("username", "jehan");
        operations.set("id", "1", 15, TimeUnit.SECONDS);
    }

    @Test
    public void testGet() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String s = operations.get("username");
        System.out.println(s);
    }
}
