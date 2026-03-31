package com.dafuweng.sales.controller;

import com.dafuweng.common.core.result.R;
import com.dafuweng.sales.domain.dto.WorkLogSubmitRequest;
import com.dafuweng.sales.domain.vo.WorkLogVO;
import com.dafuweng.sales.service.WorkLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work-log")
@RequiredArgsConstructor
public class WorkLogController {

    private final WorkLogService workLogService;

    /**
     * 提交工作日志
     */
    @PostMapping
    public R<Long> submitWorkLog(@Valid @RequestBody WorkLogSubmitRequest request) {
        return R.ok(workLogService.submitWorkLog(request));
    }

    /**
     * 工作日志详情
     */
    @GetMapping("/{id}")
    public R<WorkLogVO> getWorkLogById(@PathVariable Long id) {
        return R.ok(workLogService.getWorkLogById(id));
    }

    /**
     * 更新工作日志
     */
    @PutMapping("/{id}")
    public R<Void> updateWorkLog(@PathVariable Long id, @Valid @RequestBody WorkLogSubmitRequest request) {
        workLogService.updateWorkLog(id, request);
        return R.ok();
    }

    /**
     * 统计报表
     */
    @GetMapping("/stats")
    public R<WorkLogVO> getWorkLogStats() {
        // TODO: 实现统计报表
        return R.ok();
    }
}
