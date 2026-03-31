package com.dafuweng.sales.service;

import com.dafuweng.sales.domain.dto.WorkLogSubmitRequest;
import com.dafuweng.sales.domain.vo.WorkLogVO;

public interface WorkLogService {

    /**
     * 提交工作日志
     */
    Long submitWorkLog(WorkLogSubmitRequest request);

    /**
     * 工作日志详情
     */
    WorkLogVO getWorkLogById(Long id);

    /**
     * 更新工作日志
     */
    void updateWorkLog(Long id, WorkLogSubmitRequest request);
}
