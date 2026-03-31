package com.dafuweng.common.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * 【强制】所有分布式锁必须通过本类注入的 RedissonClient 实现
 * 【强制】禁止使用 JUC 的 ReentrantLock（单节点无效）
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:localhost}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;

    @Value("${spring.redis.password:}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        Config config = new Config();
        String addr = String.format("redis://%s:%d", host, port);
        config.useSingleServer()
              .setAddress(addr)
              .setPassword(password.isEmpty() ? null : password)
              .setDatabase(database)
              .setConnectionMinimumIdleSize(5)
              .setConnectionPoolSize(30)
              .setConnectTimeout(3000)
              .setTimeout(3000)
              .setRetryAttempts(3)
              .setRetryInterval(1500);
        return Redisson.create(config);
    }
}
