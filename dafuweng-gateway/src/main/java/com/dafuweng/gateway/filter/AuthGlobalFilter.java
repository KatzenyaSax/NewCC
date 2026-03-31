package com.dafuweng.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.dafuweng.common.core.result.R;
import com.dafuweng.gateway.constant.WhiteList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * JWT 鉴权全局过滤器
 *
 * 【强制】所有非白名单请求必须验证 JWT Token
 * 【强制】Token 验证通过后将 userId 和 token 写入 ThreadLocal，供后续服务透传
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单放行
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取 Token
        String token = extractToken(request);
        if (StrUtil.isBlank(token)) {
            return unauthorized(exchange, "未提供认证令牌");
        }

        // 验证 Token（解析 JWT，校验签名和过期时间）
        try {
            JwtUserInfo userInfo = verifyToken(token);
            // 将用户信息写入请求头，供下游服务使用
            ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", String.valueOf(userInfo.userId()))
                .header("X-Username", userInfo.username())
                .header("X-Token", token)
                .build();
            // 写入 ThreadLocal（供 FeignAuthInterceptor 使用）
            cn.hutool.core.thread.ThreadLocalUtil.put("userId", userInfo.userId());
            cn.hutool.core.thread.ThreadLocalUtil.put("token", token);
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return unauthorized(exchange, "认证令牌无效或已过期");
        }
    }

    @Override
    public int getOrder() {
        return -100; // 最高优先级
    }

    private boolean isWhiteList(String path) {
        return WhiteList.PATHS.contains(path);
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (StrUtil.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private JwtUserInfo verifyToken(String token) {
        // TODO: 实现 JWT 验证逻辑
        // 1. 解析 JWT（使用 jjwt 或 nimbus-jose-jwt）
        // 2. 验证签名（RS256 或 HS256）
        // 3. 检查过期时间
        // 4. 返回用户信息
        // 这里返回模拟数据，待实现
        return new JwtUserInfo(1L, "admin");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        R<Void> r = R.error(900102, message);
        String body = JSONUtil.toJsonStr(r);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * JWT 用户信息
     */
    public record JwtUserInfo(Long userId, String username) {}
}
