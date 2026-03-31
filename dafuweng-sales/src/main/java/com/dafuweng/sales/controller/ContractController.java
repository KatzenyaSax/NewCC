package com.dafuweng.sales.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.ContractVO;
import com.dafuweng.sales.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    /**
     * 创建合同（草稿）
     */
    @PostMapping
    public R<Long> createContract(@Valid @RequestBody ContractCreateRequest request) {
        return R.ok(contractService.createContract(request));
    }

    /**
     * 合同详情
     */
    @GetMapping("/{id}")
    public R<ContractVO> getContractById(@PathVariable Long id) {
        return R.ok(contractService.getContractById(id));
    }

    /**
     * 更新合同
     */
    @PutMapping("/{id}")
    public R<Void> updateContract(@PathVariable Long id, @Valid @RequestBody ContractCreateRequest request) {
        contractService.updateContract(id, request);
        return R.ok();
    }

    /**
     * 删除合同（草稿状态）
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return R.ok();
    }

    /**
     * 签署合同
     */
    @PutMapping("/{id}/sign")
    public R<Void> signContract(@PathVariable Long id, @Valid @RequestBody ContractSignRequest request) {
        request.setContractId(id);
        contractService.signContract(request);
        return R.ok();
    }

    /**
     * 上传合同附件
     */
    @PostMapping("/{id}/attachment")
    public R<Void> uploadAttachment(@PathVariable Long id, @Valid @RequestBody ContractAttachmentRequest request) {
        contractService.uploadAttachment(id, request);
        return R.ok();
    }

    /**
     * 确认首期服务费已付
     */
    @PutMapping("/{id}/pay-first")
    public R<Void> confirmPayFirst(@PathVariable Long id, @Valid @RequestBody ContractPayFirstRequest request) {
        request.setContractId(id);
        contractService.confirmPayFirst(request);
        return R.ok();
    }

    /**
     * 发送至金融部审核
     */
    @PostMapping("/{id}/send-finance")
    public R<Void> sendToFinance(@PathVariable Long id) {
        contractService.sendToFinance(id);
        return R.ok();
    }

    /**
     * 分页查询合同
     */
    @GetMapping("/page")
    public R<PageResult<ContractVO>> pageContract(@Valid ContractPageRequest request) {
        return R.ok(contractService.pageContract(request));
    }
}
