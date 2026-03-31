package com.dafuweng.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.converter.PerformanceConverter;
import com.dafuweng.sales.domain.dto.PerformancePageRequest;
import com.dafuweng.sales.domain.vo.PerformanceRankingVO;
import com.dafuweng.sales.domain.vo.PerformanceVO;
import com.dafuweng.sales.entity.PerformanceRecord;
import com.dafuweng.sales.mapper.PerformanceRecordMapper;
import com.dafuweng.sales.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRecordMapper performanceRecordMapper;
    private final PerformanceConverter performanceConverter;

    @Override
    public List<PerformanceVO> getRepPerformance(Long repId) {
        LambdaQueryWrapper<PerformanceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PerformanceRecord::getSalesRepId, repId)
               .eq(PerformanceRecord::getDeleted, 0)
               .orderByDesc(PerformanceRecord::getCreatedAt);

        List<PerformanceRecord> records = performanceRecordMapper.selectList(wrapper);
        return performanceConverter.toVOList(records);
    }

    @Override
    public List<PerformanceVO> getDeptPerformance(Long deptId) {
        LambdaQueryWrapper<PerformanceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PerformanceRecord::getDeptId, deptId)
               .eq(PerformanceRecord::getDeleted, 0)
               .orderByDesc(PerformanceRecord::getCreatedAt);

        List<PerformanceRecord> records = performanceRecordMapper.selectList(wrapper);
        return performanceConverter.toVOList(records);
    }

    @Override
    public List<PerformanceVO> getZonePerformance(Long zoneId) {
        LambdaQueryWrapper<PerformanceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PerformanceRecord::getZoneId, zoneId)
               .eq(PerformanceRecord::getDeleted, 0)
               .orderByDesc(PerformanceRecord::getCreatedAt);

        List<PerformanceRecord> records = performanceRecordMapper.selectList(wrapper);
        return performanceConverter.toVOList(records);
    }

    @Override
    public List<PerformanceRankingVO> getPerformanceRanking() {
        // TODO: 实现业绩排名查询
        return List.of();
    }

    @Override
    public PageResult<PerformanceVO> pagePerformance(PerformancePageRequest request) {
        Page<PerformanceRecord> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<PerformanceRecord> wrapper = new LambdaQueryWrapper<>();
        if (request.getSalesRepId() != null) {
            wrapper.eq(PerformanceRecord::getSalesRepId, request.getSalesRepId());
        }
        if (request.getDeptId() != null) {
            wrapper.eq(PerformanceRecord::getDeptId, request.getDeptId());
        }
        if (request.getZoneId() != null) {
            wrapper.eq(PerformanceRecord::getZoneId, request.getZoneId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(PerformanceRecord::getStatus, request.getStatus());
        }

        wrapper.eq(PerformanceRecord::getDeleted, 0);
        wrapper.orderByDesc(PerformanceRecord::getCreatedAt);

        IPage<PerformanceRecord> pageResult = performanceRecordMapper.selectPage(page, wrapper);
        return PageResult.of(pageResult);
    }
}
