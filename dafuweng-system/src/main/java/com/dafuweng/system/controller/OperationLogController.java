package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.OperationLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operation-log")
@RequiredArgsConstructor
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping("/{id}")
    public R<OperationLogVO> getById(@PathVariable Long id) {
        return R.ok(operationLogService.getById(id));
    }

    @GetMapping("/page")
    public R<PageResult<OperationLogVO>> page(@Valid OperationLogPageRequest request) {
        return R.ok(operationLogService.page(request));
    }
}
