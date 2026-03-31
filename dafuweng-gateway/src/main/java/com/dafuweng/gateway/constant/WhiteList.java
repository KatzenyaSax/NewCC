package com.dafuweng.gateway.constant;

import java.util.Set;

/**
 * 网关白名单
 *
 * 【强制】白名单路径不经过 JWT 鉴权
 */
public final class WhiteList {

    private WhiteList() {}

    /**
     * 无需认证的路径
     */
    public static final Set<String> PATHS = Set.of(
        "/auth/login",
        "/auth/captcha",
        "/doc.html",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/favicon.ico"
    );
}
