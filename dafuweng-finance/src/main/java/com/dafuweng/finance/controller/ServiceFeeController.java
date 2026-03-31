package com.dafuweng.finance.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordCreateRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordPageRequest;
import com.dafuweng.finance.domain.dto.ServiceFeeRecordVO;
import com.dafuweng.finance.service.ServiceFeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-fee")
@RequiredArgsConstructor
public class ServiceFeeController {

    private final ServiceFeeService serviceFeeService;

    @PostMapping("/record")
    public R<Long> create(@Valid @RequestBody ServiceFeeRecordCreateRequest request) {
        return R.ok(serviceFeeService.create(request));
    }

    @GetMapping("/{id}")
    public R<ServiceFeeRecordVO> getById(@PathVariable Long id) {
        return R.ok(serviceFeeService.getById(id));
    }

    @GetMapping("/page")
    public R<PageResult<ServiceFeeRecordVO>> page(@Valid ServiceFeeRecordPageRequest request) {
        return R.ok(serviceFeeService.page(request));
    }

    @GetMapping("/contract/{contractId}")
    public R<List<ServiceFeeRecordVO>> getByContractId(@PathVariable Long contractId) {
        return R.ok(serviceFeeService.getByContractId(contractId));
    }
}
