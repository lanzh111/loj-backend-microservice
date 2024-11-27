package com.lan.lojbackendgatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

@SpringBootTest
public class RedisIndexedSessionRepositoryTest {

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    public RedisIndexedSessionRepositoryTest(RedisIndexedSessionRepository redisIndexedSessionRepository) {
        this.redisIndexedSessionRepository = redisIndexedSessionRepository;
    }

    @Test
    public void test(){

        Session session = redisIndexedSessionRepository.findById("D302AC306611D0E2C85D42755FCD8BCB");
    }
}
