package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.SystemParamConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysParam;
import com.dafuweng.system.mapper.SysParamMapper;
import com.dafuweng.system.service.SystemParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemParamServiceImpl implements SystemParamService {

    private final SysParamMapper paramMapper;
    private final SystemParamConverter paramConverter;

    @Override
    @Transactional
    public Long create(SystemParamCreateRequest request) {
        // Check if paramKey already exists
        LambdaQueryWrapper<SysParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysParam::getParamKey, request.getParamKey());
        if (paramMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.PARAM_KEY_EXISTS);
        }

        SysParam param = paramConverter.toEntity(request);
        paramMapper.insert(param);
        return param.getId();
    }

    @Override
    @Transactional
    public void update(SystemParamUpdateRequest request) {
        SysParam param = paramMapper.selectById(request.getId());
        if (param == null) {
            throw new BusinessException(ErrorCode.PARAM_NOT_FOUND);
        }

        paramConverter.copy(request, param);
        paramMapper.updateById(param);
    }

    @Override
    @Transactional
    public void delete(String paramKey) {
        LambdaQueryWrapper<SysParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysParam::getParamKey, paramKey);
        paramMapper.delete(wrapper);
    }

    @Override
    public SysParam getByKey(String paramKey) {
        LambdaQueryWrapper<SysParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysParam::getParamKey, paramKey);
        SysParam param = paramMapper.selectOne(wrapper);
        if (param == null) {
            throw new BusinessException(ErrorCode.PARAM_NOT_FOUND);
        }
        return param;
    }

    @Override
    public String getValue(String paramKey) {
        SysParam param = getByKey(paramKey);
        return param.getParamValue();
    }

    @Override
    public PageResult<SystemParamVO> page(SystemParamPageRequest request) {
        Page<SysParam> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysParam> wrapper = new LambdaQueryWrapper<>();
        if (request.getParamGroup() != null) {
            wrapper.eq(SysParam::getParamGroup, request.getParamGroup());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysParam::getStatus, request.getStatus());
        }
        wrapper.orderByAsc(SysParam::getSortOrder);
        IPage<SysParam> result = paramMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
