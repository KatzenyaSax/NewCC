package com.dafuweng.auth.domain.dto;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {

    /**
     * JWT Token
     */
    private String accessToken;

    /**
     * 刷新 Token
     */
    private String refreshToken;

    /**
     * Token 类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（秒）
     */
    private long expiresIn;

    /**
     * 用户信息
     */
    private UserVO userInfo;
}
