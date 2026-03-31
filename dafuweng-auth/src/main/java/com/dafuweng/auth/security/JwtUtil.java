package com.dafuweng.auth.security;

import com.dafuweng.auth.entity.SysUser;
import com.dafuweng.common.core.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 生成访问令牌
     */
    public String generateToken(SysUser user, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(SysUser user, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", roles);
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析访问令牌
     */
    public SysUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 检查是否是刷新令牌
            String type = claims.get("type", String.class);
            if ("refresh".equals(type)) {
                throw new BusinessException(900102, "无效的访问令牌");
            }

            return buildUserFromClaims(claims);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(900101, "登录已过期，请重新登录");
        } catch (JwtException e) {
            throw new BusinessException(900102, "Token无效");
        }
    }

    /**
     * 解析刷新令牌
     */
    public SysUser parseRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            // 检查是否是刷新令牌
            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                throw new BusinessException(900102, "无效的刷新令牌");
            }

            return buildUserFromClaims(claims);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(900101, "登录已过期，请重新登录");
        } catch (JwtException e) {
            throw new BusinessException(900102, "Token无效");
        }
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("JWT验证失败: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SysUser buildUserFromClaims(Claims claims) {
        SysUser user = new SysUser();
        user.setId(Long.parseLong(claims.getSubject()));
        user.setUsername(claims.get("username", String.class));
        return user;
    }
}
