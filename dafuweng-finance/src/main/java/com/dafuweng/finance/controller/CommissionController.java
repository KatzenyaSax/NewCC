package com.dafuweng.finance.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.finance.domain.dto.CommissionPageRequest;
import com.dafuweng.finance.domain.dto.CommissionRecordVO;
import com.dafuweng.finance.service.CommissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commission")
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionService commissionService;

    @GetMapping("/page")
    public R<PageResult<CommissionRecordVO>> page(@Valid CommissionPageRequest request) {
        return R.ok(commissionService.page(request));
    }

    @GetMapping("/rep/{repId}")
    public R<List<CommissionRecordVO>> getByRepId(@PathVariable Long repId) {
        return R.ok(commissionService.getByRepId(repId));
    }

    @PostMapping("/confirm/{id}")
    public R<Void> confirm(@PathVariable Long id) {
        commissionService.confirm(id);
        return R.ok();
    }

    @PostMapping("/grant/{id}")
    public R<Void> grant(@PathVariable Long id) {
        commissionService.grant(id);
        return R.ok();
    }
}
