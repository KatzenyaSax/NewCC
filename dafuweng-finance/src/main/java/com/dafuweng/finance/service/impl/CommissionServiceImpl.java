package com.dafuweng.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.converter.CommissionConverter;
import com.dafuweng.finance.domain.dto.CommissionPageRequest;
import com.dafuweng.finance.domain.dto.CommissionRecordVO;
import com.dafuweng.finance.entity.CommissionRecord;
import com.dafuweng.finance.mapper.CommissionRecordMapper;
import com.dafuweng.finance.service.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommissionServiceImpl implements CommissionService {

    private final CommissionRecordMapper commissionRecordMapper;
    private final CommissionConverter commissionConverter;

    @Override
    public PageResult<CommissionRecordVO> page(CommissionPageRequest request) {
        Page<CommissionRecord> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<CommissionRecord> wrapper = new LambdaQueryWrapper<>();

        if (request.getSalesRepId() != null) {
            wrapper.eq(CommissionRecord::getSalesRepId, request.getSalesRepId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(CommissionRecord::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(CommissionRecord::getCreatedAt);
        IPage<CommissionRecord> result = commissionRecordMapper.selectPage(page, wrapper);
        return PageResult.of(result, commissionConverter.toVOList(result.getRecords()));
    }

    @Override
    public List<CommissionRecordVO> getByRepId(Long repId) {
        LambdaQueryWrapper<CommissionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommissionRecord::getSalesRepId, repId)
                .orderByDesc(CommissionRecord::getCreatedAt);
        List<CommissionRecord> records = commissionRecordMapper.selectList(wrapper);
        return commissionConverter.toVOList(records);
    }

    @Override
    @Transactional
    public void confirm(Long id) {
        CommissionRecord entity = commissionRecordMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        if (entity.getStatus() != 1) {
            throw new BusinessException(ErrorCode.DATA_STATUS_ERROR);
        }

        entity.setStatus(2);
        entity.setConfirmTime(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        commissionRecordMapper.updateById(entity);
    }

    @Override
    @Transactional
    public void grant(Long id) {
        CommissionRecord entity = commissionRecordMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        if (entity.getStatus() != 2) {
            throw new BusinessException(ErrorCode.DATA_STATUS_ERROR);
        }

        entity.setStatus(3);
        entity.setGrantTime(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        commissionRecordMapper.updateById(entity);
    }
}
