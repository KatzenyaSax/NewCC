package com.dafuweng.sales.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.ContractVO;

public interface ContractService {

    /**
     * 创建合同（草稿）
     */
    Long createContract(ContractCreateRequest request);

    /**
     * 合同详情
     */
    ContractVO getContractById(Long id);

    /**
     * 更新合同
     */
    void updateContract(Long id, ContractCreateRequest request);

    /**
     * 删除合同（草稿状态）
     */
    void deleteContract(Long id);

    /**
     * 签署合同
     */
    void signContract(ContractSignRequest request);

    /**
     * 上传合同附件
     */
    void uploadAttachment(Long contractId, ContractAttachmentRequest request);

    /**
     * 确认首期服务费已付
     */
    void confirmPayFirst(ContractPayFirstRequest request);

    /**
     * 发送至金融部审核
     */
    void sendToFinance(Long contractId);

    /**
     * 分页查询合同
     */
    PageResult<ContractVO> pageContract(ContractPageRequest request);
}
