package com.dafuweng.common.redis.service;

/**
 * Redis 通用服务接口
 *
 * 【推荐】所有 Redis 操作通过本接口，避免直接注入 RedissonClient
 */
public interface RedisService {

    /**
     * 设置字符串值
     */
    void set(String key, String value);

    /**
     * 设置字符串值，带过期时间（秒）
     */
    void set(String key, String value, long timeoutSeconds);

    /**
     * 获取字符串值
     */
    String get(String key);

    /**
     * 删除 key
     */
    Boolean delete(String key);

    /**
     * 判断 key 是否存在
     */
    Boolean hasKey(String key);

    /**
     * 设置过期时间（秒）
     */
    Boolean expire(String key, long timeoutSeconds);

    /**
     * 获取过期时间（秒）
     */
    Long getExpire(String key);

    /**
     * 获取 Redisson 分布式锁
     */
    org.redisson.api.RLock getLock(String lockKey);
}
