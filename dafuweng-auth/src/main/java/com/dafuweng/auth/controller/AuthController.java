package com.dafuweng.auth.controller;

import com.dafuweng.auth.domain.dto.LoginRequest;
import com.dafuweng.auth.domain.dto.LoginResponse;
import com.dafuweng.auth.domain.dto.RefreshTokenRequest;
import com.dafuweng.auth.domain.dto.UserVO;
import com.dafuweng.auth.service.AuthService;
import com.dafuweng.common.core.result.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 账号密码登录
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return R.ok(authService.login(request));
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public R<Void> logout(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        authService.logout(actualToken);
        return R.ok();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    public R<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return R.ok(authService.refresh(request));
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    public R<UserVO> getUserInfo(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        return R.ok(authService.getUserInfo(actualToken));
    }
}
