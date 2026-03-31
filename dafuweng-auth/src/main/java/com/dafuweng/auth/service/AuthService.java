package com.dafuweng.auth.service;

import com.dafuweng.auth.domain.dto.LoginRequest;
import com.dafuweng.auth.domain.dto.LoginResponse;
import com.dafuweng.auth.domain.dto.RefreshTokenRequest;
import com.dafuweng.auth.domain.dto.UserVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     */
    void logout(String token);

    /**
     * 刷新Token
     */
    LoginResponse refresh(RefreshTokenRequest request);

    /**
     * 获取当前用户信息
     */
    UserVO getUserInfo(String token);
}
