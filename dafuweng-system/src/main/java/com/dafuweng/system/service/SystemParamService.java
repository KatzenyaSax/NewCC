package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysParam;

public interface SystemParamService {

    Long create(SystemParamCreateRequest request);

    void update(SystemParamUpdateRequest request);

    void delete(String paramKey);

    SysParam getByKey(String paramKey);

    String getValue(String paramKey);

    PageResult<SystemParamVO> page(SystemParamPageRequest request);
}
