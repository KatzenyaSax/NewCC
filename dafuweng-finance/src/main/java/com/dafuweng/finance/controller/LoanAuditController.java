package com.dafuweng.finance.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.finance.domain.dto.*;
import com.dafuweng.finance.service.LoanAuditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan-audit")
@RequiredArgsConstructor
public class LoanAuditController {

    private final LoanAuditService loanAuditService;

    @GetMapping("/received")
    public R<PageResult<LoanAuditSimpleVO>> received(@RequestParam(defaultValue = "1") Integer pageNum,
                                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<LoanAuditSimpleVO> page = new PageResult<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        return R.ok(loanAuditService.received(page));
    }

    @PostMapping("/{id}/receive")
    public R<Long> receive(@PathVariable Long id, @Valid @RequestBody LoanAuditReceiveRequest request) {
        request.setContractId(id);
        return R.ok(loanAuditService.receive(request));
    }

    @PostMapping("/{id}/review")
    public R<Long> review(@PathVariable Long id, @Valid @RequestBody LoanAuditReviewRequest request) {
        request.setAuditId(id);
        return R.ok(loanAuditService.review(request));
    }

    @PostMapping("/{id}/submit-bank")
    public R<Long> submitBank(@PathVariable Long id, @Valid @RequestBody LoanAuditSubmitBankRequest request) {
        request.setAuditId(id);
        return R.ok(loanAuditService.submitBank(request));
    }

    @PostMapping("/{id}/bank-result")
    public R<Long> bankResult(@PathVariable Long id, @Valid @RequestBody LoanAuditBankResultRequest request) {
        request.setAuditId(id);
        return R.ok(loanAuditService.bankResult(request));
    }

    @PostMapping("/{id}/reject")
    public R<Long> reject(@PathVariable Long id, @Valid @RequestBody LoanAuditRejectRequest request) {
        request.setAuditId(id);
        return R.ok(loanAuditService.reject(request));
    }

    @GetMapping("/{id}")
    public R<LoanAuditVO> getById(@PathVariable Long id) {
        return R.ok(loanAuditService.getById(id));
    }

    @GetMapping("/{id}/history")
    public R<List<LoanAuditRecordVO>> history(@PathVariable Long id) {
        return R.ok(loanAuditService.history(id));
    }
}
