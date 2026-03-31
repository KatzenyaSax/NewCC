package com.dafuweng.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.converter.ServiceFeeConverter;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordCreateRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordPageRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordVO;
import com.dafuweng.finance.entity.ServiceFeeRecord;
import com.dafuweng.finance.mapper.ServiceFeeRecordMapper;
import com.dafuweng.finance.service.ServiceFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceFeeServiceImpl implements ServiceFeeService {

    private final ServiceFeeRecordMapper serviceFeeRecordMapper;
    private final ServiceFeeConverter serviceFeeConverter;

    @Override
    @Transactional
    public Long create(ServiceFeeRecordCreateRequest request) {
        if (request.getAmount().compareTo(request.getShouldAmount()) > 0) {
            throw new BusinessException(ErrorCode.SERVICEFEE_AMOUNT_ERROR);
        }

        ServiceFeeRecord entity = serviceFeeConverter.toEntity(request);
        entity.setPaymentStatus(1);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        serviceFeeRecordMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public ServiceFeeRecordVO getById(Long id) {
        ServiceFeeRecord entity = serviceFeeRecordMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.DATA_NOT_FOUND);
        }
        return serviceFeeConverter.toVO(entity);
    }

    @Override
    public PageResult<ServiceFeeRecordVO> page(ServiceFeeRecordPageRequest request) {
        Page<ServiceFeeRecord> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<ServiceFeeRecord> wrapper = new LambdaQueryWrapper<>();

        if (request.getContractId() != null) {
            wrapper.eq(ServiceFeeRecord::getContractId, request.getContractId());
        }
        if (request.getFeeType() != null) {
            wrapper.eq(ServiceFeeRecord::getFeeType, request.getFeeType());
        }
        if (request.getPaymentStatus() != null) {
            wrapper.eq(ServiceFeeRecord::getPaymentStatus, request.getPaymentStatus());
        }
        if (request.getAccountantId() != null) {
            wrapper.eq(ServiceFeeRecord::getAccountantId, request.getAccountantId());
        }
        if (request.getStartDate() != null) {
            wrapper.ge(ServiceFeeRecord::getPaymentDate, request.getStartDate());
        }
        if (request.getEndDate() != null) {
            wrapper.le(ServiceFeeRecord::getPaymentDate, request.getEndDate());
        }

        wrapper.orderByDesc(ServiceFeeRecord::getCreatedAt);
        IPage<ServiceFeeRecord> result = serviceFeeRecordMapper.selectPage(page, wrapper);
        return PageResult.of(result, serviceFeeConverter.toVOList(result.getRecords()));
    }

    @Override
    public List<ServiceFeeRecordVO> getByContractId(Long contractId) {
        LambdaQueryWrapper<ServiceFeeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceFeeRecord::getContractId, contractId)
                .orderByDesc(ServiceFeeRecord::getCreatedAt);
        List<ServiceFeeRecord> records = serviceFeeRecordMapper.selectList(wrapper);
        return serviceFeeConverter.toVOList(records);
    }
}
