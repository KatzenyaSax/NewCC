package com.dafuweng.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.converter.FinanceProductConverter;
import com.dafuweng.finance.domain.dto.FinanceProductCreateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductPageRequest;
import com.dafuweng.finance.domain.dto.FinanceProductUpdateRequest;
import com.dafuweng.finance.domain.dto.FinanceProductVO;
import com.dafuweng.finance.entity.FinanceProduct;
import com.dafuweng.finance.mapper.FinanceProductMapper;
import com.dafuweng.finance.service.BankService;
import com.dafuweng.finance.service.FinanceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceProductServiceImpl implements FinanceProductService {

    private final FinanceProductMapper financeProductMapper;
    private final FinanceProductConverter financeProductConverter;
    private final BankService bankService;

    @Override
    @Transactional
    public Long create(FinanceProductCreateRequest request) {
        FinanceProduct entity = financeProductConverter.toEntity(request);

        var bank = bankService.getEntityById(request.getBankId());
        if (bank == null) {
            throw new BusinessException(ErrorCode.BANK_NOT_FOUND);
        }
        entity.setBankName(bank.getBankName());

        entity.setStatus(1);
        entity.setCreatedAt(LocalDateTime.now());
        financeProductMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void update(FinanceProductUpdateRequest request) {
        FinanceProduct entity = getEntityById(request.getId());
        financeProductConverter.copy(request, entity);

        if (request.getBankId() != null) {
            var bank = bankService.getEntityById(request.getBankId());
            if (bank == null) {
                throw new BusinessException(ErrorCode.BANK_NOT_FOUND);
            }
            entity.setBankName(bank.getBankName());
        }

        entity.setUpdatedAt(LocalDateTime.now());
        financeProductMapper.updateById(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        FinanceProduct entity = getEntityById(id);
        entity.setStatus(0);
        entity.setOfflineTime(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        financeProductMapper.updateById(entity);
    }

    @Override
    public FinanceProductVO getById(Long id) {
        FinanceProduct entity = getEntityById(id);
        return financeProductConverter.toVO(entity);
    }

    @Override
    public List<FinanceProductVO> list() {
        LambdaQueryWrapper<FinanceProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FinanceProduct::getStatus, 1)
                .orderByAsc(FinanceProduct::getSortOrder);
        List<FinanceProduct> list = financeProductMapper.selectList(wrapper);
        return financeProductConverter.toVOList(list);
    }

    @Override
    public PageResult<FinanceProductVO> page(FinanceProductPageRequest request) {
        Page<FinanceProduct> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<FinanceProduct> wrapper = new LambdaQueryWrapper<>();

        if (request.getProductName() != null) {
            wrapper.like(FinanceProduct::getProductName, request.getProductName());
        }
        if (request.getBankId() != null) {
            wrapper.eq(FinanceProduct::getBankId, request.getBankId());
        }
        if (request.getMinLoanAmount() != null) {
            wrapper.le(FinanceProduct::getMinAmount, request.getMinLoanAmount());
        }
        if (request.getMaxLoanAmount() != null) {
            wrapper.ge(FinanceProduct::getMaxAmount, request.getMaxLoanAmount());
        }
        if (request.getTerm() != null) {
            wrapper.le(FinanceProduct::getMinTerm, request.getTerm())
                    .ge(FinanceProduct::getMaxTerm, request.getTerm());
        }
        if (request.getStatus() != null) {
            wrapper.eq(FinanceProduct::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(FinanceProduct::getCreatedAt);
        IPage<FinanceProduct> result = financeProductMapper.selectPage(page, wrapper);
        return PageResult.of(result, financeProductConverter.toVOList(result.getRecords()));
    }

    @Override
    public FinanceProduct getEntityById(Long id) {
        FinanceProduct entity = financeProductMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return entity;
    }
}
