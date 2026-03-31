package com.dafuweng.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.converter.LoanAuditConverter;
import com.dafuweng.finance.domain.dto.*;
import com.dafuweng.finance.entity.LoanAudit;
import com.dafuweng.finance.entity.LoanAuditRecord;
import com.dafuweng.finance.mapper.LoanAuditMapper;
import com.dafuweng.finance.mapper.LoanAuditRecordMapper;
import com.dafuweng.finance.service.FinanceProductService;
import com.dafuweng.finance.service.LoanAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanAuditServiceImpl implements LoanAuditService {

    private final LoanAuditMapper loanAuditMapper;
    private final LoanAuditRecordMapper loanAuditRecordMapper;
    private final LoanAuditConverter loanAuditConverter;
    private final FinanceProductService financeProductService;

    @Override
    @Transactional
    public Long receive(LoanAuditReceiveRequest request) {
        LoanAudit entity = new LoanAudit();
        entity.setContractId(request.getContractId());
        entity.setAuditStatus(1);
        entity.setCreatedAt(LocalDateTime.now());
        loanAuditMapper.insert(entity);

        LoanAuditRecord record = new LoanAuditRecord();
        record.setLoanAuditId(entity.getId());
        record.setOperatorId(1L);
        record.setAction("receive");
        record.setContent(request.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        loanAuditRecordMapper.insert(record);

        return entity.getId();
    }

    @Override
    @Transactional
    public Long review(LoanAuditReviewRequest request) {
        LoanAudit entity = getEntityById(request.getAuditId());
        if (entity.getAuditStatus() != 1 && entity.getAuditStatus() != 2) {
            throw new BusinessException(ErrorCode.AUDIT_STATUS_ERROR);
        }

        var product = financeProductService.getEntityById(request.getRecommendedProductId());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        entity.setRecommendedProductId(request.getRecommendedProductId());
        entity.setApprovedAmount(request.getApprovedAmount());
        entity.setApprovedTerm(request.getApprovedTerm());
        entity.setApprovedInterestRate(request.getApprovedInterestRate());
        entity.setAuditOpinion(request.getAuditOpinion());
        entity.setAuditStatus(2);
        entity.setAuditDate(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        loanAuditMapper.updateById(entity);

        LoanAuditRecord record = new LoanAuditRecord();
        record.setLoanAuditId(entity.getId());
        record.setOperatorId(1L);
        record.setAction("review");
        record.setContent(request.getAuditOpinion());
        record.setAttachmentUrls(request.getAttachmentUrls() != null ? String.join(",", request.getAttachmentUrls()) : null);
        record.setCreatedAt(LocalDateTime.now());
        loanAuditRecordMapper.insert(record);

        return entity.getId();
    }

    @Override
    @Transactional
    public Long submitBank(LoanAuditSubmitBankRequest request) {
        LoanAudit entity = getEntityById(request.getAuditId());
        if (entity.getAuditStatus() != 2) {
            throw new BusinessException(ErrorCode.AUDIT_SUBMIT_BANK_ERROR);
        }

        entity.setBankId(request.getBankId());
        entity.setBankAuditStatus("pending");
        entity.setBankApplyTime(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        loanAuditMapper.updateById(entity);

        LoanAuditRecord record = new LoanAuditRecord();
        record.setLoanAuditId(entity.getId());
        record.setOperatorId(1L);
        record.setAction("submit_bank");
        record.setContent(request.getRemark());
        record.setCreatedAt(LocalDateTime.now());
        loanAuditRecordMapper.insert(record);

        return entity.getId();
    }

    @Override
    @Transactional
    public Long bankResult(LoanAuditBankResultRequest request) {
        LoanAudit entity = getEntityById(request.getAuditId());
        if (!"pending".equals(entity.getBankAuditStatus()) && !"in_review".equals(entity.getBankAuditStatus())) {
            throw new BusinessException(ErrorCode.AUDIT_BANK_RESULT_ERROR);
        }

        entity.setBankAuditStatus(request.getBankAuditStatus());
        entity.setBankFeedbackContent(request.getBankFeedbackContent());
        entity.setBankFeedbackTime(LocalDateTime.now());

        if ("approved".equals(request.getBankAuditStatus())) {
            entity.setActualLoanAmount(request.getActualLoanAmount());
            entity.setActualInterestRate(request.getActualInterestRate());
            entity.setLoanGrantedDate(request.getLoanGrantedDate());
            entity.setAuditStatus(3);
        } else if ("rejected".equals(request.getBankAuditStatus())) {
            entity.setRejectReason(request.getBankFeedbackContent());
            entity.setAuditStatus(4);
        }

        entity.setUpdatedAt(LocalDateTime.now());
        loanAuditMapper.updateById(entity);

        LoanAuditRecord record = new LoanAuditRecord();
        record.setLoanAuditId(entity.getId());
        record.setOperatorId(1L);
        record.setAction("bank_result");
        record.setContent(request.getBankFeedbackContent());
        record.setCreatedAt(LocalDateTime.now());
        loanAuditRecordMapper.insert(record);

        return entity.getId();
    }

    @Override
    @Transactional
    public Long reject(LoanAuditRejectRequest request) {
        LoanAudit entity = getEntityById(request.getAuditId());
        if (entity.getAuditStatus() == 3 || entity.getAuditStatus() == 4) {
            throw new BusinessException(ErrorCode.AUDIT_STATUS_ERROR);
        }

        entity.setAuditStatus(4);
        entity.setRejectReason(request.getRejectReason());
        entity.setUpdatedAt(LocalDateTime.now());
        loanAuditMapper.updateById(entity);

        LoanAuditRecord record = new LoanAuditRecord();
        record.setLoanAuditId(entity.getId());
        record.setOperatorId(1L);
        record.setAction("reject");
        record.setContent(request.getRejectReason());
        record.setCreatedAt(LocalDateTime.now());
        loanAuditRecordMapper.insert(record);

        return entity.getId();
    }

    @Override
    public LoanAuditVO getById(Long id) {
        LoanAudit entity = getEntityById(id);
        LoanAuditVO vo = loanAuditConverter.toVO(entity);

        LambdaQueryWrapper<LoanAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoanAuditRecord::getLoanAuditId, id)
                .orderByDesc(LoanAuditRecord::getCreatedAt);
        List<LoanAuditRecord> records = loanAuditRecordMapper.selectList(wrapper);
        vo.setAuditRecords(loanAuditConverter.toRecordVOList(records));

        return vo;
    }

    @Override
    public PageResult<LoanAuditSimpleVO> received(PageResult<LoanAuditSimpleVO> pageRequest) {
        Page<LoanAudit> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        LambdaQueryWrapper<LoanAudit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoanAudit::getAuditStatus, 1)
                .orderByDesc(LoanAudit::getCreatedAt);
        IPage<LoanAudit> result = loanAuditMapper.selectPage(page, wrapper);
        return PageResult.of(result, loanAuditConverter.toSimpleVOList(result.getRecords()));
    }

    @Override
    public List<LoanAuditRecordVO> history(Long id) {
        LambdaQueryWrapper<LoanAuditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoanAuditRecord::getLoanAuditId, id)
                .orderByDesc(LoanAuditRecord::getCreatedAt);
        List<LoanAuditRecord> records = loanAuditRecordMapper.selectList(wrapper);
        return loanAuditConverter.toRecordVOList(records);
    }

    @Override
    public LoanAudit getEntityById(Long id) {
        LoanAudit entity = loanAuditMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.AUDIT_NOT_FOUND);
        }
        return entity;
    }
}
