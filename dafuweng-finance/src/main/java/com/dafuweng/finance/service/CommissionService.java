package com.dafuweng.finance.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.finance.domain.dto.CommissionRecordVO;
import com.dafuweng.finance.domain.dto.CommissionPageRequest;

import java.util.List;

public interface CommissionService {

    PageResult<CommissionRecordVO> page(CommissionPageRequest request);

    List<CommissionRecordVO> getByRepId(Long repId);

    void confirm(Long id);

    void grant(Long id);
}
