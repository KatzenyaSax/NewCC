package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.OperationLogConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysOperationLog;
import com.dafuweng.system.mapper.SysOperationLogMapper;
import com.dafuweng.system.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final SysOperationLogMapper operationLogMapper;
    private final OperationLogConverter operationLogConverter;

    @Override
    public SysOperationLog getById(Long id) {
        SysOperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        return log;
    }

    @Override
    public PageResult<OperationLogVO> page(OperationLogPageRequest request) {
        Page<SysOperationLog> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (request.getUserId() != null) {
            wrapper.eq(SysOperationLog::getUserId, request.getUserId());
        }
        if (request.getModule() != null) {
            wrapper.eq(SysOperationLog::getModule, request.getModule());
        }
        wrapper.orderByDesc(SysOperationLog::getCreatedAt);
        IPage<SysOperationLog> result = operationLogMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
