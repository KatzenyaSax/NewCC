package com.dafuweng.sales.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.sales.domain.dto.PerformancePageRequest;
import com.dafuweng.sales.domain.vo.PerformanceRankingVO;
import com.dafuweng.sales.domain.vo.PerformanceVO;
import com.dafuweng.sales.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    /**
     * 销售代表业绩
     */
    @GetMapping("/rep/{repId}")
    public R<List<PerformanceVO>> getRepPerformance(@PathVariable Long repId) {
        return R.ok(performanceService.getRepPerformance(repId));
    }

    /**
     * 部门业绩
     */
    @GetMapping("/dept/{deptId}")
    public R<List<PerformanceVO>> getDeptPerformance(@PathVariable Long deptId) {
        return R.ok(performanceService.getDeptPerformance(deptId));
    }

    /**
     * 战区业绩
     */
    @GetMapping("/zone/{zoneId}")
    public R<List<PerformanceVO>> getZonePerformance(@PathVariable Long zoneId) {
        return R.ok(performanceService.getZonePerformance(zoneId));
    }

    /**
     * 业绩排名
     */
    @GetMapping("/ranking")
    public R<List<PerformanceRankingVO>> getPerformanceRanking() {
        return R.ok(performanceService.getPerformanceRanking());
    }

    /**
     * 业绩分析
     */
    @GetMapping("/analysis")
    public R<Object> getPerformanceAnalysis() {
        // TODO: 实现业绩分析
        return R.ok();
    }

    /**
     * 分页查询业绩记录
     */
    @GetMapping("/page")
    public R<PageResult<PerformanceVO>> pagePerformance(@Valid PerformancePageRequest request) {
        return R.ok(performanceService.pagePerformance(request));
    }
}
