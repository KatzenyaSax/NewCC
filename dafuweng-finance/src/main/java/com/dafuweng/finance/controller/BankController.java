package com.dafuweng.finance.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.finance.domain.dto.BankPageRequest;
import com.dafuweng.finance.domain.dto.BankVO;
import com.dafuweng.finance.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/{id}")
    public R<BankVO> getById(@PathVariable Long id) {
        return R.ok(bankService.getById(id));
    }

    @GetMapping("/list")
    public R<List<BankVO>> list() {
        return R.ok(bankService.list());
    }

    @GetMapping("/page")
    public R<PageResult<BankVO>> page(@Valid BankPageRequest request) {
        return R.ok(bankService.page(request));
    }
}
