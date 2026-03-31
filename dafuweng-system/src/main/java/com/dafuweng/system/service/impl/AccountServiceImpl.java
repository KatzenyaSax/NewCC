package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.AccountConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysAccount;
import com.dafuweng.system.mapper.SysAccountMapper;
import com.dafuweng.system.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final SysAccountMapper accountMapper;
    private final AccountConverter accountConverter;

    @Override
    @Transactional
    public Long create(AccountCreateRequest request) {
        // Check if username already exists
        LambdaQueryWrapper<SysAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAccount::getUsername, request.getUsername());
        if (accountMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }

        // Check if phone already exists
        if (request.getPhone() != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysAccount::getPhone, request.getPhone());
            if (accountMapper.exists(wrapper)) {
                throw new BusinessException(ErrorCode.ACCOUNT_PHONE_EXISTS);
            }
        }

        SysAccount account = accountConverter.toEntity(request);
        // Set default password (in real scenario, encode it)
        account.setPassword("$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiWWGFIgxG8e"); // admin123
        accountMapper.insert(account);
        return account.getId();
    }

    @Override
    @Transactional
    public void update(AccountUpdateRequest request) {
        SysAccount account = accountMapper.selectById(request.getId());
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        // Check if phone already exists (excluding current account)
        if (request.getPhone() != null) {
            LambdaQueryWrapper<SysAccount> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysAccount::getPhone, request.getPhone())
                   .ne(SysAccount::getId, request.getId());
            if (accountMapper.exists(wrapper)) {
                throw new BusinessException(ErrorCode.ACCOUNT_PHONE_EXISTS);
            }
        }

        accountConverter.copy(request, account);
        accountMapper.updateById(account);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        accountMapper.deleteById(id);
    }

    @Override
    public SysAccount getById(Long id) {
        SysAccount account = accountMapper.selectById(id);
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    @Override
    @Transactional
    public void resetPassword(AccountResetPasswordRequest request) {
        SysAccount account = accountMapper.selectById(request.getId());
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        // Reset to default password (in real scenario, generate random password)
        account.setPassword("$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiWWGFIgxG8e");
        accountMapper.updateById(account);
    }

    @Override
    @Transactional
    public void changePassword(AccountChangePasswordRequest request) {
        SysAccount account = accountMapper.selectById(request.getId());
        if (account == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        // In real scenario, verify old password and encode new password
        account.setPassword("$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiWWGFIgxG8e");
        accountMapper.updateById(account);
    }

    @Override
    public PageResult<AccountVO> page(AccountPageRequest request) {
        Page<SysAccount> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysAccount> wrapper = new LambdaQueryWrapper<>();
        if (request.getDeptId() != null) {
            wrapper.eq(SysAccount::getDeptId, request.getDeptId());
        }
        if (request.getZoneId() != null) {
            wrapper.eq(SysAccount::getZoneId, request.getZoneId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysAccount::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(SysAccount::getCreatedAt);
        IPage<SysAccount> result = accountMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
