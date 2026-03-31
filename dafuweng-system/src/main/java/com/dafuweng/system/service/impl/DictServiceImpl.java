package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.DictConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysDict;
import com.dafuweng.system.mapper.SysDictMapper;
import com.dafuweng.system.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {

    private final SysDictMapper dictMapper;
    private final DictConverter dictConverter;

    @Override
    @Transactional
    public Long create(DictCreateRequest request) {
        // Check if dictType+dictCode already exists
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, request.getDictType())
               .eq(SysDict::getDictCode, request.getDictCode());
        if (dictMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }

        SysDict dict = dictConverter.toEntity(request);
        dictMapper.insert(dict);
        return dict.getId();
    }

    @Override
    @Transactional
    public void update(DictUpdateRequest request) {
        SysDict dict = dictMapper.selectById(request.getId());
        if (dict == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }

        dictConverter.copy(request, dict);
        dictMapper.updateById(dict);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dictMapper.deleteById(id);
    }

    @Override
    public SysDict getById(Long id) {
        SysDict dict = dictMapper.selectById(id);
        if (dict == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        return dict;
    }

    @Override
    public List<DictVO> getByType(String dictType) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictType, dictType)
               .eq(SysDict::getStatus, 1)
               .orderByAsc(SysDict::getSortOrder);
        List<SysDict> dicts = dictMapper.selectList(wrapper);
        return dictConverter.toVOList(dicts);
    }

    @Override
    public PageResult<DictVO> page(DictPageRequest request) {
        Page<SysDict> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (request.getDictType() != null) {
            wrapper.eq(SysDict::getDictType, request.getDictType());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysDict::getStatus, request.getStatus());
        }
        wrapper.orderByAsc(SysDict::getSortOrder);
        IPage<SysDict> result = dictMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
