package com.dafuweng.sales.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.domain.dto.PerformancePageRequest;
import com.dafuweng.sales.domain.vo.PerformanceRankingVO;
import com.dafuweng.sales.domain.vo.PerformanceVO;

import java.util.List;

public interface PerformanceService {

    /**
     * 销售代表业绩
     */
    List<PerformanceVO> getRepPerformance(Long repId);

    /**
     * 部门业绩
     */
    List<PerformanceVO> getDeptPerformance(Long deptId);

    /**
     * 战区业绩
     */
    List<PerformanceVO> getZonePerformance(Long zoneId);

    /**
     * 业绩排名
     */
    List<PerformanceRankingVO> getPerformanceRanking();

    /**
     * 分页查询业绩记录
     */
    PageResult<PerformanceVO> pagePerformance(PerformancePageRequest request);
}
