package com.dafuweng.common.feign.interceptor;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign 认证拦截器
 *
 * 【强制】服务间调用时自动透传 Authorization 头
 * 【强制】从 ThreadLocal 获取当前请求的 Token（Gateway 写入）
 */
@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate template) {
        // 从 ThreadLocal 获取当前请求的 Token
        String token = getCurrentToken();
        if (StrUtil.isNotBlank(token)) {
            template.header(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
        }
    }

    /**
     * 从 ThreadLocal 获取当前请求的 JWT Token
     * Gateway 的 AuthGlobalFilter 在验证 Token 后会写入 ThreadLocal
     */
    private String getCurrentToken() {
        try {
            Object token = cn.hutool.core.thread.ThreadLocalUtil.get("token");
            return token != null ? token.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
