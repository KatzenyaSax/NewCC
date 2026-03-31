package com.dafuweng.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.converter.BankConverter;
import com.dafuweng.finance.domain.dto.BankPageRequest;
import com.dafuweng.finance.domain.dto.BankVO;
import com.dafuweng.finance.entity.Bank;
import com.dafuweng.finance.mapper.BankMapper;
import com.dafuweng.finance.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankMapper bankMapper;
    private final BankConverter bankConverter;

    @Override
    public BankVO getById(Long id) {
        Bank entity = getEntityById(id);
        return bankConverter.toVO(entity);
    }

    @Override
    public List<BankVO> list() {
        LambdaQueryWrapper<Bank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Bank::getStatus, 1)
                .orderByAsc(Bank::getSortOrder);
        List<Bank> list = bankMapper.selectList(wrapper);
        return bankConverter.toVOList(list);
    }

    @Override
    public PageResult<BankVO> page(BankPageRequest request) {
        Page<Bank> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<Bank> wrapper = new LambdaQueryWrapper<>();

        if (request.getBankName() != null) {
            wrapper.like(Bank::getBankName, request.getBankName());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Bank::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(Bank::getCreatedAt);
        IPage<Bank> result = bankMapper.selectPage(page, wrapper);
        return PageResult.of(result, bankConverter.toVOList(result.getRecords()));
    }

    @Override
    public Bank getEntityById(Long id) {
        Bank entity = bankMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.BANK_NOT_FOUND);
        }
        return entity;
    }
}
