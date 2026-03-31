package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysOperationLog;

public interface OperationLogService {

    SysOperationLog getById(Long id);

    PageResult<OperationLogVO> page(OperationLogPageRequest request);
}
