package com.dafuweng.gateway.config;

import com.dafuweng.gateway.filter.AuthGlobalFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关全局配置
 */
@Configuration
public class GatewayConfig {

    /**
     * JWT 鉴权全局过滤器
     * 拦截所有请求，验证 Token（白名单除外）
     */
    @Bean
    @ConditionalOnMissingBean(AuthGlobalFilter.class)
    public AuthGlobalFilter authGlobalFilter() {
        return new AuthGlobalFilter();
    }
}
