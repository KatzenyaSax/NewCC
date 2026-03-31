package com.dafuweng.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dafuweng.auth.converter.SysUserConverter;
import com.dafuweng.auth.domain.dto.LoginRequest;
import com.dafuweng.auth.domain.dto.LoginResponse;
import com.dafuweng.auth.domain.dto.RefreshTokenRequest;
import com.dafuweng.auth.domain.dto.UserVO;
import com.dafuweng.auth.entity.SysRole;
import com.dafuweng.auth.entity.SysUser;
import com.dafuweng.auth.entity.SysUserRole;
import com.dafuweng.auth.mapper.SysUserMapper;
import com.dafuweng.auth.security.JwtUtil;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final SysUserConverter sysUserConverter;
    private final JwtUtil jwtUtil;
    private final RedissonClient redissonClient;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 校验验证码（简化处理，实际应从Redis校验）
        // TODO: 实现验证码校验逻辑

        // 2. 查询用户
        SysUser user = sysUserMapper.selectOne(
            new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername())
                .eq(SysUser::getDeleted, 0)
        );

        if (user == null) {
            throw new BusinessException(ErrorCode.AUTH_LOGIN_FAILED);
        }

        // 3. 校验账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.AUTH_ACCOUNT_DISABLED);
        }

        // 4. 校验账号锁定
        if (user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.AUTH_ACCOUNT_LOCKED);
        }

        // 5. 校验密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 增加登录错误次数
            incrementLoginErrorCount(user);
            throw new BusinessException(ErrorCode.AUTH_LOGIN_FAILED);
        }

        // 6. 重置登录错误次数
        resetLoginErrorCount(user);

        // 7. 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);

        // 8. 获取用户角色
        List<String> roles = getUserRoles(user.getId());

        // 9. 生成Token
        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtUtil.generateToken(user, roles));
        response.setRefreshToken(jwtUtil.generateRefreshToken(user, roles));
        response.setExpiresIn(expiration / 1000);
        response.setUserInfo(sysUserConverter.toVO(user));

        return response;
    }

    @Override
    public void logout(String token) {
        // 将token加入黑名单
        String blacklistKey = BLACKLIST_PREFIX + token;
        RBucket<String> bucket = redissonClient.getBucket(blacklistKey);
        bucket.set("1", expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public LoginResponse refresh(RefreshTokenRequest request) {
        // 解析refresh token
        SysUser user = jwtUtil.parseRefreshToken(request.getRefreshToken());

        // 查询用户
        SysUser dbUser = sysUserMapper.selectById(user.getId());
        if (dbUser == null || dbUser.getDeleted() == 1) {
            throw new BusinessException(ErrorCode.AUTH_REFRESH_TOKEN_FAILED);
        }

        // 校验账号状态
        if (dbUser.getStatus() == 0) {
            throw new BusinessException(ErrorCode.AUTH_ACCOUNT_DISABLED);
        }

        // 获取用户角色
        List<String> roles = getUserRoles(user.getId());

        // 生成新Token
        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtUtil.generateToken(dbUser, roles));
        response.setRefreshToken(jwtUtil.generateRefreshToken(dbUser, roles));
        response.setExpiresIn(expiration / 1000);
        response.setUserInfo(sysUserConverter.toVO(dbUser));

        return response;
    }

    @Override
    public UserVO getUserInfo(String token) {
        SysUser user = jwtUtil.parseToken(token);
        SysUser dbUser = sysUserMapper.selectById(user.getId());
        if (dbUser == null || dbUser.getDeleted() == 1) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        return sysUserConverter.toVO(dbUser);
    }

    private void incrementLoginErrorCount(SysUser user) {
        user.setLoginErrorCount(user.getLoginErrorCount() + 1);
        if (user.getLoginErrorCount() >= 5) {
            // 锁定30分钟
            user.setLockTime(LocalDateTime.now().plusMinutes(30));
        }
        sysUserMapper.updateById(user);
    }

    private void resetLoginErrorCount(SysUser user) {
        user.setLoginErrorCount(0);
        user.setLockTime(null);
    }

    private List<String> getUserRoles(Long userId) {
        // TODO: 实现从sys_user_role和sys_role表查询用户角色
        return List.of();
    }
}
