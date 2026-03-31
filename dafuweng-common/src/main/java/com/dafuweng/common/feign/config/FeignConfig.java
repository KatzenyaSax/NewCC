package com.dafuweng.common.feign.config;

import com.dafuweng.common.feign.interceptor.FeignAuthInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 全局配置
 *
 * 【强制】所有 Feign Client 必须使用本配置类
 * 【强制】FeignAuthInterceptor 自动透传 Authorization 头
 */
@Configuration
public class FeignConfig {

    /**
     * Feign 日志级别
     */
    @Bean
    @ConditionalOnMissingBean(Logger.Level.class)
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * Feign 认证拦截器 — 自动透传 JWT Token
     */
    @Bean
    @ConditionalOnMissingBean(RequestInterceptor.class)
    public RequestInterceptor feignAuthInterceptor() {
        return new FeignAuthInterceptor();
    }
}
