package com.dafuweng.finance.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.finance.domain.dto.*;
import com.dafuweng.finance.service.FinanceProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class FinanceProductController {

    private final FinanceProductService financeProductService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody FinanceProductCreateRequest request) {
        return R.ok(financeProductService.create(request));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody FinanceProductUpdateRequest request) {
        request.setId(id);
        financeProductService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        financeProductService.delete(id);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R<FinanceProductVO> getById(@PathVariable Long id) {
        return R.ok(financeProductService.getById(id));
    }

    @GetMapping("/list")
    public R<List<FinanceProductVO>> list() {
        return R.ok(financeProductService.list());
    }

    @GetMapping("/page")
    public R<PageResult<FinanceProductVO>> page(@Valid FinanceProductPageRequest request) {
        return R.ok(financeProductService.page(request));
    }
}
