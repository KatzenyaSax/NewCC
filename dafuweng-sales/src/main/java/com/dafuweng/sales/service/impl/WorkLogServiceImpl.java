package com.dafuweng.sales.service.impl;

import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.sales.converter.WorkLogConverter;
import com.dafuweng.sales.domain.dto.WorkLogSubmitRequest;
import com.dafuweng.sales.domain.vo.WorkLogVO;
import com.dafuweng.sales.entity.WorkLog;
import com.dafuweng.sales.mapper.WorkLogMapper;
import com.dafuweng.sales.service.WorkLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {

    private final WorkLogMapper workLogMapper;
    private final WorkLogConverter workLogConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitWorkLog(WorkLogSubmitRequest request) {
        WorkLog workLog = workLogConverter.toEntity(request);
        // TODO: 设置salesRepId等字段
        workLogMapper.insert(workLog);
        return workLog.getId();
    }

    @Override
    public WorkLogVO getWorkLogById(Long id) {
        WorkLog workLog = workLogMapper.selectById(id);
        if (workLog == null) {
            throw new BusinessException(ErrorCode.WORKLOG_NOT_FOUND);
        }
        return workLogConverter.toVO(workLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkLog(Long id, WorkLogSubmitRequest request) {
        WorkLog existWorkLog = workLogMapper.selectById(id);
        if (existWorkLog == null) {
            throw new BusinessException(ErrorCode.WORKLOG_NOT_FOUND);
        }

        WorkLog workLog = workLogConverter.toEntity(request);
        workLog.setId(id);
        workLogMapper.updateById(workLog);
    }
}
