package com.dafuweng.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.converter.ContractConverter;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.ContractVO;
import com.dafuweng.sales.entity.Contract;
import com.dafuweng.sales.entity.ContractAttachment;
import com.dafuweng.sales.enums.ContractStatus;
import com.dafuweng.sales.mapper.ContractAttachmentMapper;
import com.dafuweng.sales.mapper.ContractMapper;
import com.dafuweng.sales.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractMapper contractMapper;
    private final ContractAttachmentMapper contractAttachmentMapper;
    private final ContractConverter contractConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createContract(ContractCreateRequest request) {
        Contract contract = contractConverter.toEntity(request);
        // 设置合同编号
        contract.setContractNo(generateContractNo());
        // 计算首期和二期服务费
        BigDecimal serviceFee1 = request.getContractAmount().multiply(request.getServiceFeeRate());
        contract.setServiceFee1(serviceFee1);
        contract.setServiceFee2(serviceFee1);
        contract.setServiceFee1Paid(0);
        contract.setServiceFee2Paid(0);
        contract.setStatus(ContractStatus.DRAFT.getCode());
        // TODO: 设置salesRepId, deptId, createdBy等字段

        contractMapper.insert(contract);
        return contract.getId();
    }

    @Override
    public ContractVO getContractById(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        ContractVO vo = contractConverter.toVO(contract);
        // 查询附件
        LambdaQueryWrapper<ContractAttachment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContractAttachment::getContractId, id)
               .eq(ContractAttachment::getDeleted, 0);
        List<ContractAttachment> attachments = contractAttachmentMapper.selectList(wrapper);
        vo.setAttachments(contractConverter.toAttachmentVOList(attachments));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContract(Long id, ContractCreateRequest request) {
        Contract existContract = contractMapper.selectById(id);
        if (existContract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        if (existContract.getStatus() != ContractStatus.DRAFT.getCode()) {
            throw new BusinessException(ErrorCode.CONTRACT_STATUS_ERROR);
        }

        Contract contract = contractConverter.toEntity(request);
        contract.setId(id);
        contractMapper.updateById(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContract(Long id) {
        Contract contract = contractMapper.selectById(id);
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        if (contract.getStatus() != ContractStatus.DRAFT.getCode()) {
            throw new BusinessException(ErrorCode.CONTRACT_STATUS_ERROR);
        }
        contractMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signContract(ContractSignRequest request) {
        Contract contract = contractMapper.selectById(request.getContractId());
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        if (contract.getStatus() != ContractStatus.DRAFT.getCode()) {
            throw new BusinessException(ErrorCode.CONTRACT_STATUS_ERROR);
        }

        // 更新合同状态
        contract.setStatus(ContractStatus.SIGNED.getCode());
        contract.setSignDate(request.getSignDate());
        // TODO: 保存纸质合同编号 paperContractNo

        // 保存附件
        for (ContractAttachmentRequest attachmentRequest : request.getAttachments()) {
            ContractAttachment attachment = new ContractAttachment();
            attachment.setContractId(request.getContractId());
            attachment.setAttachmentType(attachmentRequest.getAttachmentType());
            attachment.setFileUrl(attachmentRequest.getFileUrl());
            attachment.setFileName(attachmentRequest.getFileName());
            attachment.setFileSize(attachmentRequest.getFileSize());
            contractAttachmentMapper.insert(attachment);
        }

        contractMapper.updateById(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadAttachment(Long contractId, ContractAttachmentRequest request) {
        Contract contract = contractMapper.selectById(contractId);
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }

        ContractAttachment attachment = new ContractAttachment();
        attachment.setContractId(contractId);
        attachment.setAttachmentType(request.getAttachmentType());
        attachment.setFileUrl(request.getFileUrl());
        attachment.setFileName(request.getFileName());
        attachment.setFileSize(request.getFileSize());
        contractAttachmentMapper.insert(attachment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayFirst(ContractPayFirstRequest request) {
        Contract contract = contractMapper.selectById(request.getContractId());
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        if (contract.getStatus() != ContractStatus.SIGNED.getCode()) {
            throw new BusinessException(ErrorCode.CONTRACT_STATUS_ERROR);
        }

        contract.setStatus(ContractStatus.FIRST_FEE_PAID.getCode());
        // TODO: 设置serviceFee1PayDate
        contractMapper.updateById(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendToFinance(Long contractId) {
        Contract contract = contractMapper.selectById(contractId);
        if (contract == null) {
            throw new BusinessException(ErrorCode.CONTRACT_NOT_FOUND);
        }
        if (contract.getStatus() != ContractStatus.FIRST_FEE_PAID.getCode()) {
            throw new BusinessException(ErrorCode.CONTRACT_SEND_FINANCE_ERROR);
        }

        contract.setStatus(ContractStatus.IN_AUDIT.getCode());
        contract.setFinanceSendTime(java.time.LocalDateTime.now());
        contractMapper.updateById(contract);
    }

    @Override
    public PageResult<ContractVO> pageContract(ContractPageRequest request) {
        Page<Contract> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (request.getContractNo() != null) {
            wrapper.eq(Contract::getContractNo, request.getContractNo());
        }
        if (request.getCustomerId() != null) {
            wrapper.eq(Contract::getCustomerId, request.getCustomerId());
        }
        if (request.getSalesRepId() != null) {
            wrapper.eq(Contract::getSalesRepId, request.getSalesRepId());
        }
        if (request.getDeptId() != null) {
            wrapper.eq(Contract::getDeptId, request.getDeptId());
        }
        if (request.getProductId() != null) {
            wrapper.eq(Contract::getProductId, request.getProductId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Contract::getStatus, request.getStatus());
        }

        wrapper.eq(Contract::getDeleted, 0);
        wrapper.orderByDesc(Contract::getCreatedAt);

        IPage<Contract> pageResult = contractMapper.selectPage(page, wrapper);
        return PageResult.of(pageResult);
    }

    private String generateContractNo() {
        // TODO: 实现合同编号生成逻辑
        return "CT" + System.currentTimeMillis();
    }
}
